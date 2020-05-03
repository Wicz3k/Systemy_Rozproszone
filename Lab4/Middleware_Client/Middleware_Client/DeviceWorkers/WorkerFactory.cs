using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Ice;

namespace Middleware_Client.DeviceWorkers
{
    public class WorkerFactory
    {
        public static DeviceWorker createWorker(KeyValuePair<string, DeviceType> newDevice, Communicator communicator, string connectionConfiguration)
        {
            string connectionString = newDevice.Value.getCategory() + "/" + newDevice.Key + connectionConfiguration;
            try
            {
                switch (newDevice.Value)
                {
                    case DeviceType.Camera:
                        return new CameraWorker(communicator, connectionString);
                    case DeviceType.CentralHeating:
                        return new CentralHeatingWorker(communicator, connectionString);
                    case DeviceType.SlowWeatherStation:
                    case DeviceType.WeatherStation:
                        return new WeatherStationWorker(communicator, connectionString);
                    default: return null;
                }
            }
            catch(System.Exception e)
            {
                Console.Write(e);
                Console.Write(e.Message);
                Console.Write(e.StackTrace);
            }
            return null;
        }
    }
}
