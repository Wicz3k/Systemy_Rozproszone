using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Middleware_Client.DeviceWorkers
{
    public enum DeviceType
    {
        Camera, CentralHeating, SlowWeatherStation, WeatherStation
    }
    static class DeviceTypeMethods
    {
        public static string getCategory(this DeviceType type)
        {
            switch (type)
            {
                case DeviceType.Camera:
                    return "camera";
                case DeviceType.CentralHeating:
                    return "heating";
                case DeviceType.SlowWeatherStation:
                case DeviceType.WeatherStation:
                    return "weather";
                default:
                    throw new ArgumentOutOfRangeException();
            }
        }
    }
}
