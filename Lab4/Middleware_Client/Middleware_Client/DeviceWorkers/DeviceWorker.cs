using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Middleware_Client.DeviceWorkers
{
    public interface DeviceWorker
    {
        void disconnect();
        void message(string message);
    }
}
