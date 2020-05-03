using Middleware_Client.DeviceWorkers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Middleware_Client
{
    public class Client
    {
        public Client(Dictionary<string, DeviceType> availableDevices)
        {
            this.availableDevices = availableDevices;
        }

        public void startWork(string[] args)
        {
            try
            {
                using (Ice.Communicator communicator = Ice.Util.initialize(ref args))
                {
                    bool stop = false;
                    DeviceWorker device = null;
                    Console.WriteLine("What do you want to do?");
                    while (!stop)
                    {
                        string message = Console.ReadLine();
                        switch (message)
                        {
                            case "exit":
                                if (device != null)
                                {
                                    device.disconnect();
                                    device = null;
                                }
                                stop = true;
                                break;
                            case "disconnect":
                                if (device != null)
                                {
                                    device.disconnect();
                                    device = null;
                                }
                                break;
                            case "device-list":
                                Console.Write(AvailableDevicesString);
                                break;
                            case "connect":
                                if (device != null)
                                {
                                    Console.WriteLine("You need to disconnect from all current device");
                                    break;
                                }

                                while (true)
                                {
                                    Console.WriteLine("Select device name, or write 'cancel' to return to main menu");
                                    string deviceName = Console.ReadLine();

                                    if(deviceName == "cancel")
                                    {
                                        break;
                                    }

                                    if (!AvailableDevices.ContainsKey(deviceName))
                                    {
                                        Console.WriteLine("Could not find: " + deviceName);
                                    }
                                    else
                                    {
                                        var newDevice = AvailableDevices.FirstOrDefault(dev => dev.Key == deviceName);
                                        device = WorkerFactory.createWorker(newDevice, communicator, ":tcp -h localhost -p 11000");
                                        if(device == null)
                                        {
                                            Console.WriteLine("Could not connect to device");
                                        }
                                        else
                                        {
                                            Console.WriteLine("Success");
                                            break;
                                        }
                                    }
                                }
                                break;
                            default:
                                if (device != null)
                                {
                                    device.message(message);
                                }
                                else
                                {
                                    Console.WriteLine("Maybe try: 'device-list'\n");
                                }
                                break;
                        }
                    }
                }
            }
            catch(Exception e)
            {
                Console.Write(e);
                Console.Write(e.Message);
                Console.Write(e.StackTrace);
            }
            Console.ReadLine();
        }

        private string AvailableDevicesString
        {
            get
            {
                StringBuilder result = new StringBuilder();
                foreach (var record in AvailableDevices)
                {
                    result.AppendLine(readAvailableDevice(record));
                }
                return result.ToString();
            }
        }

        private static string readAvailableDevice(KeyValuePair<string, DeviceType> availableDevice)
        {
            return availableDevice.Key + " / " + availableDevice.Value.getCategory();
        }

        private Dictionary<string, DeviceType> availableDevices = null;

        private Dictionary<string, DeviceType> AvailableDevices
        {
            get
            {
                return availableDevices;
            }
        }
    }
}
