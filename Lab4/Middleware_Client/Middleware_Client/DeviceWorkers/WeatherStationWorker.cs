using Ice;
using Smart;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Middleware_Client.DeviceWorkers
{
    class WeatherStationWorker : DeviceWorker
    {
        private Communicator communicator;
        private string connectionString;
        private WeatherStationPrx device;

        public WeatherStationWorker(Communicator communicator, string connectionString)
        {
            this.communicator = communicator;
            this.connectionString = connectionString;
            connectToDevice(communicator, connectionString);
        }

        private void connectToDevice(Communicator communicator, string connectionString)
        {
            var obj = communicator.stringToProxy(connectionString);
            device = WeatherStationPrxHelper.checkedCast(obj);
            if (device == null)
            {
                throw new ApplicationException("Invalid proxy");
            }
        }

        public void disconnect()
        {
            device = null;
        }

        public void message(string message)
        {
            bool retry = false;
            int retries = 0;
            do
            {
                try
                {
                    retry = false;
                    consumeMessage(message);
                }
                catch (Smart.ArgumentOutOfRangeException e)
                {
                    Console.WriteLine("ArgumentOutOfRangeException");
                    Console.WriteLine("ArgumentName: " + e.argumentName);
                    Console.WriteLine("ExpectedMaximum: " + e.expectedMaximum);
                    Console.WriteLine("ExpectedMinimum: " + e.expectedMinimum);
                }
                catch (ConnectionLostException e)
                {
                    if (retries++ < 3)
                    {
                        Console.WriteLine("Connection lost, retry: " + retries);
                        retry = true;
                    }
                    else
                    {
                        Console.WriteLine("Connection lost");
                    }

                }
                catch (System.Exception e)
                {
                    Console.Write(e);
                    Console.Write(e.Message);
                }
            }
            while (retry);
        }

        public void consumeMessage(string message)
        {
            switch (message)
            {
                case "actions":
                    Console.Write(DeviceActions);
                    break;
                case "get-temp":
                    Console.WriteLine(device.getTemperature());
                    break;
                case "get-hum":
                    Console.WriteLine(device.getHumidity());
                    break;
                case "get-pressure":
                    Console.WriteLine(device.getPressure());
                    break;
                case "get-data":
                    var data = device.getWeatherData();
                    Console.WriteLine(WeatherDataAsString(data));
                    break;
                case "get-daily-data":
                    var dailyData = device.getDailyWeather();
                    foreach(var record in dailyData)
                        Console.WriteLine(record.Key + "  =>   " + WeatherDataAsString(record.Value));
                    break;
                case "get-errors":
                    var errors = device.getErrorCodes();
                    if (!errors.Any())
                    {
                        Console.WriteLine("No error codes");
                        break;
                    }
                    foreach (var error in errors)
                    {
                        Console.WriteLine("Code: " + error.code + "\tCount: " + error.count);
                    }
                    break;
                case "reset-errors":
                    device.resetErrorCodes();
                    break;
                default:
                    Console.WriteLine("Maybe try: 'actions'\n");
                    break;
            }
        }

        private static string WeatherDataAsString(weatherData data)
        {
            return "Temperature: " + data.temperature + "'C\tHumidity: " + data.humidity + "%\tPressure: " + data.pressure + "hPa";
        }

        public string DeviceActions
        {
            get
            {
                return "get-temp, get-pressure, get-hum, get-data, get-daily-data, get-errors, reset-errors\n";
            }
        }
    }
}
