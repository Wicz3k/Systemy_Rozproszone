package sr.grpc.gen;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.27.0)",
    comments = "Source: names.proto")
public final class NameStreamGrpc {

  private NameStreamGrpc() {}

  public static final String SERVICE_NAME = "streaming.NameStream";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<sr.grpc.gen.Task,
      sr.grpc.gen.NameMessage> getSubscribeChannelMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "subscribeChannel",
      requestType = sr.grpc.gen.Task.class,
      responseType = sr.grpc.gen.NameMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<sr.grpc.gen.Task,
      sr.grpc.gen.NameMessage> getSubscribeChannelMethod() {
    io.grpc.MethodDescriptor<sr.grpc.gen.Task, sr.grpc.gen.NameMessage> getSubscribeChannelMethod;
    if ((getSubscribeChannelMethod = NameStreamGrpc.getSubscribeChannelMethod) == null) {
      synchronized (NameStreamGrpc.class) {
        if ((getSubscribeChannelMethod = NameStreamGrpc.getSubscribeChannelMethod) == null) {
          NameStreamGrpc.getSubscribeChannelMethod = getSubscribeChannelMethod =
              io.grpc.MethodDescriptor.<sr.grpc.gen.Task, sr.grpc.gen.NameMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "subscribeChannel"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sr.grpc.gen.Task.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sr.grpc.gen.NameMessage.getDefaultInstance()))
              .setSchemaDescriptor(new NameStreamMethodDescriptorSupplier("subscribeChannel"))
              .build();
        }
      }
    }
    return getSubscribeChannelMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NameStreamStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NameStreamStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NameStreamStub>() {
        @java.lang.Override
        public NameStreamStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NameStreamStub(channel, callOptions);
        }
      };
    return NameStreamStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NameStreamBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NameStreamBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NameStreamBlockingStub>() {
        @java.lang.Override
        public NameStreamBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NameStreamBlockingStub(channel, callOptions);
        }
      };
    return NameStreamBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static NameStreamFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NameStreamFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NameStreamFutureStub>() {
        @java.lang.Override
        public NameStreamFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NameStreamFutureStub(channel, callOptions);
        }
      };
    return NameStreamFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class NameStreamImplBase implements io.grpc.BindableService {

    /**
     */
    public void subscribeChannel(sr.grpc.gen.Task request,
        io.grpc.stub.StreamObserver<sr.grpc.gen.NameMessage> responseObserver) {
      asyncUnimplementedUnaryCall(getSubscribeChannelMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSubscribeChannelMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                sr.grpc.gen.Task,
                sr.grpc.gen.NameMessage>(
                  this, METHODID_SUBSCRIBE_CHANNEL)))
          .build();
    }
  }

  /**
   */
  public static final class NameStreamStub extends io.grpc.stub.AbstractAsyncStub<NameStreamStub> {
    private NameStreamStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NameStreamStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NameStreamStub(channel, callOptions);
    }

    /**
     */
    public void subscribeChannel(sr.grpc.gen.Task request,
        io.grpc.stub.StreamObserver<sr.grpc.gen.NameMessage> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getSubscribeChannelMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class NameStreamBlockingStub extends io.grpc.stub.AbstractBlockingStub<NameStreamBlockingStub> {
    private NameStreamBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NameStreamBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NameStreamBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<sr.grpc.gen.NameMessage> subscribeChannel(
        sr.grpc.gen.Task request) {
      return blockingServerStreamingCall(
          getChannel(), getSubscribeChannelMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class NameStreamFutureStub extends io.grpc.stub.AbstractFutureStub<NameStreamFutureStub> {
    private NameStreamFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NameStreamFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NameStreamFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_SUBSCRIBE_CHANNEL = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final NameStreamImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(NameStreamImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SUBSCRIBE_CHANNEL:
          serviceImpl.subscribeChannel((sr.grpc.gen.Task) request,
              (io.grpc.stub.StreamObserver<sr.grpc.gen.NameMessage>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class NameStreamBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    NameStreamBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return sr.grpc.gen.StreamingNames.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("NameStream");
    }
  }

  private static final class NameStreamFileDescriptorSupplier
      extends NameStreamBaseDescriptorSupplier {
    NameStreamFileDescriptorSupplier() {}
  }

  private static final class NameStreamMethodDescriptorSupplier
      extends NameStreamBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    NameStreamMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (NameStreamGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new NameStreamFileDescriptorSupplier())
              .addMethod(getSubscribeChannelMethod())
              .build();
        }
      }
    }
    return result;
  }
}
