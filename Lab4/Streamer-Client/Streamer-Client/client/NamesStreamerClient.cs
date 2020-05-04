using Grpc.Core;
using System;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using static Streaming.NameStream;

namespace Streamer_Client.client
{
    public class NamesStreamerClient
    {
        string adress;
        int port;
        Channel channel;
        Mutex channelMutex;
        public NamesStreamerClient(string adress, int port)
        {
            this.adress = adress;
            this.port = port;
            channelMutex = new Mutex();
            connectChannel(adress, port);
        }

        private void connectChannel(string adress, int port)
        {
            channel = new Channel(adress, port, ChannelCredentials.Insecure);
        }

        internal void work()
        {
            bool stop = false;
            while (!stop)
            {
                string job = Console.ReadLine();
                switch (job)
                {
                    case "scenarios":
                        StringBuilder sb = new StringBuilder();
                        sb.AppendLine("sub1: CH1");
                        sb.AppendLine("sub2: CH4");
                        sb.AppendLine("sub3: CH1, CH4");
                        sb.AppendLine("sub4: CH0, CH1, CH2, CH3, CH4");
                        Console.Write(sb.ToString());
                        break;
                    case "sub1":
                        createSubscription(Streaming.Channel.Ch1);
                        break;
                    case "sub2":
                        createSubscription(Streaming.Channel.Ch4);
                        break;
                    case "sub3":
                        createSubscription(Streaming.Channel.Ch1);
                        createSubscription(Streaming.Channel.Ch4);
                        break;
                    case "sub4":
                        createSubscription(Streaming.Channel.Ch0);
                        createSubscription(Streaming.Channel.Ch1);
                        createSubscription(Streaming.Channel.Ch2);
                        createSubscription(Streaming.Channel.Ch3);
                        createSubscription(Streaming.Channel.Ch4);
                        break;
                    default:
                        Console.WriteLine("Try 'scenarios'.");
                        break;
                }
            }
        }

        private void createSubscription(Streaming.Channel ch)
        {
            var task = new Task(() => subscribeAsync(ch));
            task.Start();
        }

        private async Task subscribeAsync(Streaming.Channel ch)
        {
            var streamClient = new NameStreamClient(channel);
            while (true)
            {
                try
                {
                    var channelMessage = new Streaming.Task();
                    channelMessage.Channel = ch;
                    var stream = streamClient.subscribeChannel(channelMessage).ResponseStream;
                    while (await stream.MoveNext())
                    {
                        var message = stream.Current;
                        var sb = new StringBuilder();
                        sb.Append(message.Channel + " ");
                        sb.Append(message.Temperature + " ");
                        sb.Append(message.NumberOfPeoples + " ");
                        foreach (var info in message.Information)
                        {
                            sb.Append(info + " ");
                        }
                        Console.WriteLine(sb.ToString());
                    }
                }
                catch(RpcException e)
                {
                    Console.WriteLine("Lost connection on channel: " + ch);
                    var state = channel.State;
                    if (state != ChannelState.Ready)
                    {
                        await channel.TryWaitForStateChangedAsync(state, DateTime.UtcNow.AddSeconds(5));
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine(e);
                }
            }
        }
    }
}
