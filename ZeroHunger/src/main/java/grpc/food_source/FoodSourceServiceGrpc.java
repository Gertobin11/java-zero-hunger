package grpc.food_source;

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
 * <pre>
 * The food source service contains a mapping of addresses that contain items available 
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: food_source.proto")
public final class FoodSourceServiceGrpc {

  private FoodSourceServiceGrpc() {}

  public static final String SERVICE_NAME = "food_source.FoodSourceService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<grpc.common.Address,
      grpc.common.FoodItem> getStreamAvailableFoodItemsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StreamAvailableFoodItems",
      requestType = grpc.common.Address.class,
      responseType = grpc.common.FoodItem.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<grpc.common.Address,
      grpc.common.FoodItem> getStreamAvailableFoodItemsMethod() {
    io.grpc.MethodDescriptor<grpc.common.Address, grpc.common.FoodItem> getStreamAvailableFoodItemsMethod;
    if ((getStreamAvailableFoodItemsMethod = FoodSourceServiceGrpc.getStreamAvailableFoodItemsMethod) == null) {
      synchronized (FoodSourceServiceGrpc.class) {
        if ((getStreamAvailableFoodItemsMethod = FoodSourceServiceGrpc.getStreamAvailableFoodItemsMethod) == null) {
          FoodSourceServiceGrpc.getStreamAvailableFoodItemsMethod = getStreamAvailableFoodItemsMethod = 
              io.grpc.MethodDescriptor.<grpc.common.Address, grpc.common.FoodItem>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "food_source.FoodSourceService", "StreamAvailableFoodItems"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.common.Address.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.common.FoodItem.getDefaultInstance()))
                  .setSchemaDescriptor(new FoodSourceServiceMethodDescriptorSupplier("StreamAvailableFoodItems"))
                  .build();
          }
        }
     }
     return getStreamAvailableFoodItemsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FoodSourceServiceStub newStub(io.grpc.Channel channel) {
    return new FoodSourceServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FoodSourceServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new FoodSourceServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static FoodSourceServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new FoodSourceServiceFutureStub(channel);
  }

  /**
   * <pre>
   * The food source service contains a mapping of addresses that contain items available 
   * </pre>
   */
  public static abstract class FoodSourceServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * takes an argument of address and streams the items at that address
     * </pre>
     */
    public void streamAvailableFoodItems(grpc.common.Address request,
        io.grpc.stub.StreamObserver<grpc.common.FoodItem> responseObserver) {
      asyncUnimplementedUnaryCall(getStreamAvailableFoodItemsMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getStreamAvailableFoodItemsMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                grpc.common.Address,
                grpc.common.FoodItem>(
                  this, METHODID_STREAM_AVAILABLE_FOOD_ITEMS)))
          .build();
    }
  }

  /**
   * <pre>
   * The food source service contains a mapping of addresses that contain items available 
   * </pre>
   */
  public static final class FoodSourceServiceStub extends io.grpc.stub.AbstractStub<FoodSourceServiceStub> {
    private FoodSourceServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private FoodSourceServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FoodSourceServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new FoodSourceServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * takes an argument of address and streams the items at that address
     * </pre>
     */
    public void streamAvailableFoodItems(grpc.common.Address request,
        io.grpc.stub.StreamObserver<grpc.common.FoodItem> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getStreamAvailableFoodItemsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * The food source service contains a mapping of addresses that contain items available 
   * </pre>
   */
  public static final class FoodSourceServiceBlockingStub extends io.grpc.stub.AbstractStub<FoodSourceServiceBlockingStub> {
    private FoodSourceServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private FoodSourceServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FoodSourceServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new FoodSourceServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * takes an argument of address and streams the items at that address
     * </pre>
     */
    public java.util.Iterator<grpc.common.FoodItem> streamAvailableFoodItems(
        grpc.common.Address request) {
      return blockingServerStreamingCall(
          getChannel(), getStreamAvailableFoodItemsMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * The food source service contains a mapping of addresses that contain items available 
   * </pre>
   */
  public static final class FoodSourceServiceFutureStub extends io.grpc.stub.AbstractStub<FoodSourceServiceFutureStub> {
    private FoodSourceServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private FoodSourceServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FoodSourceServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new FoodSourceServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_STREAM_AVAILABLE_FOOD_ITEMS = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final FoodSourceServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(FoodSourceServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_STREAM_AVAILABLE_FOOD_ITEMS:
          serviceImpl.streamAvailableFoodItems((grpc.common.Address) request,
              (io.grpc.stub.StreamObserver<grpc.common.FoodItem>) responseObserver);
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

  private static abstract class FoodSourceServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    FoodSourceServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpc.food_source.FoodSource.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("FoodSourceService");
    }
  }

  private static final class FoodSourceServiceFileDescriptorSupplier
      extends FoodSourceServiceBaseDescriptorSupplier {
    FoodSourceServiceFileDescriptorSupplier() {}
  }

  private static final class FoodSourceServiceMethodDescriptorSupplier
      extends FoodSourceServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    FoodSourceServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (FoodSourceServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new FoodSourceServiceFileDescriptorSupplier())
              .addMethod(getStreamAvailableFoodItemsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
