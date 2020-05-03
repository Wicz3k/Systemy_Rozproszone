using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Ice;
using Smart;

namespace Middleware_Client.DeviceWorkers
{
    public class CameraWorker : DeviceWorker
    {
        private Communicator communicator;
        private string connectionString;
        private CameraPrx device;

        public CameraWorker(Communicator communicator, string connectionString)
        {
            this.communicator = communicator;
            this.connectionString = connectionString;
            connectToDevice(communicator, connectionString);
        }

        private void connectToDevice(Communicator communicator, string connectionString)
        {
            var obj = communicator.stringToProxy(connectionString);
            device = CameraPrxHelper.checkedCast(obj);
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
                catch (Ice.ConnectionLostException e)
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
                case "get-pos":
                    var pos = device.getPosition();
                    Console.WriteLine("P: " + pos.pan + "\tT: " + pos.tilt + "\tZ: " + pos.zoom);
                    break;
                case "get-lock":
                    bool locked = device.getLocked();
                    Console.WriteLine(locked);
                    break;
                case "lock":
                    device.setLocked(true);
                    break;
                case "unlock":
                    device.setLocked(false);
                    break;
                case "set-pos1":
                    Console.WriteLine(device.setPosition(new cameraPosition(40, 50, 8)));
                    break;
                case "set-pos2":
                    Console.WriteLine(device.setPosition(new cameraPosition(-50, 0, 1)));
                    break;
                case "set-pos3":
                    Console.WriteLine(device.setPosition(new cameraPosition(-1000, 500, 8)));
                    break;
                case "set-pos-async":
                    var task = device.setPositionAsync(new cameraPosition(40, 50, 8));
                        task.ContinueWith(job => Console.WriteLine("\n" + job.Result));
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
                return "set-pos1, set-pos2, set-pos3, set-pos-async, get-pos, lock, unlock, get-lock, get-errors, reset-errors\n";
            }
        }
    }
}
