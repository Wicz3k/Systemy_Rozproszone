using Streamer_Client.client;

namespace Streamer_Client
{
    class Program
    {
        static void Main(string[] args)
        {
            var client = new NamesStreamerClient("localhost", 50060);
            client.work();
        }
    }
}
