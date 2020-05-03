using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Ice;
using Smart;

namespace Middleware_Client.DeviceWorkers
{
    class CentralHeatingWorker : DeviceWorker
    {
        private Communicator communicator;
        private string connectionString;
        private CentralHeatingPrx device;

        public CentralHeatingWorker(Communicator communicator, string connectionString)
        {
            this.communicator = communicator;
            this.connectionString = connectionString;
            connectToDevice(communicator, connectionString);
        }

        private void connectToDevice(Communicator communicator, string connectionString)
        {
            var obj = communicator.stringToProxy(connectionString);
            device = CentralHeatingPrxHelper.checkedCast(obj);
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
                catch (ConnectionLostException)
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
                    var temp = device.getDesiredTemperature();
                    Console.WriteLine(temp);
                    break;
                case "get-mode":
                    var mode = device.getHeatingMode();
                    Console.WriteLine(mode);
                    break;
                case "set-temp1":
                    device.setTemperature(15);
                    break;
                case "set-temp2":
                    device.setTemperature(27);
                    break;
                case "set-mode1":
                    device.setHeatingMode(heatingMode.HOLIDAY);
                    break;
                case "set-mode2":
                    device.setHeatingMode(heatingMode.OFF);
                    break;
                case "set-mode3":
                    device.setHeatingMode(heatingMode.ON);
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

        public string DeviceActions
        {
            get
            {
                return "set-temp1, set-temp2, get-temp, set-mode1, set-mode2, set-mode3, get-mode, get-errors, reset-errors\n";
            }
        }
    }
}
