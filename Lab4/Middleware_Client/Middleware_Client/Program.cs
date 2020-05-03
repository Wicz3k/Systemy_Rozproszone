
using Middleware_Client.DeviceWorkers;
using System.Collections.Generic;

namespace Middleware_Client
{
    class Program
    {
        static void Main(string[] args)
        {
            Client client = new Client(AvailableDevices);
            client.startWork(args);
        }


        private static Dictionary<string, DeviceType> AvailableDevices
        {
            get
            {
                Dictionary<string, DeviceType> availableDevices = new Dictionary<string, DeviceType>();
                availableDevices.Add("weather1", DeviceType.WeatherStation);
                availableDevices.Add("weather2", DeviceType.WeatherStation);
                availableDevices.Add("weatherSlow", DeviceType.SlowWeatherStation);
                availableDevices.Add("cam1", DeviceType.Camera);
                availableDevices.Add("cam2", DeviceType.Camera);
                availableDevices.Add("heating1", DeviceType.CentralHeating);
                return availableDevices;
            }
        }
    }
}
