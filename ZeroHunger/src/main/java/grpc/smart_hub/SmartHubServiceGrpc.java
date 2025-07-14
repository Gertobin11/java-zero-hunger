package grpc.smart_hub;

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
 * The SmartHubService manages user food requests.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: smart_hub.proto")
public final class SmartHubServiceGrpc {

  private SmartHubServiceGrpc() {}

  public static final String SERVICE_NAME = "smart_hub.SmartHubService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<grpc.common.FoodRequest,
      grpc.smart_hub.StatusResponse> getHandleFoodRequestsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "handleFoodRequests",
      requestType = grpc.common.FoodRequest.class,
      responseType = grpc.smart_hub.StatusResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc.common.FoodRequest,
      grpc.smart_hub.StatusResponse> getHandleFoodRequestsMethod() {
    io.grpc.MethodDescriptor<grpc.common.FoodRequest, grpc.smart_hub.StatusResponse> getHandleFoodRequestsMethod;
    if ((getHandleFoodRequestsMethod = SmartHubServiceGrpc.getHandleFoodRequestsMethod) == null) {
      synchronized (SmartHubServiceGrpc.class) {
        if ((getHandleFoodRequestsMethod = SmartHubServiceGrpc.getHandleFoodRequestsMethod) == null) {
          SmartHubServiceGrpc.getHandleFoodRequestsMethod = getHandleFoodRequestsMethod = 
              io.grpc.MethodDescriptor.<grpc.common.FoodRequest, grpc.smart_hub.StatusResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "smart_hub.SmartHubService", "handleFoodRequests"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.common.FoodRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.smart_hub.StatusResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new SmartHubServiceMethodDescriptorSupplier("handleFoodRequests"))
                  .build();
          }
        }
     }
     return getHandleFoodRequestsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpc.smart_hub.StatusRequest,
      grpc.smart_hub.StatusResponse> getStatusUpdateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "statusUpdate",
      requestType = grpc.smart_hub.StatusRequest.class,
      responseType = grpc.smart_hub.StatusResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc.smart_hub.StatusRequest,
      grpc.smart_hub.StatusResponse> getStatusUpdateMethod() {
    io.grpc.MethodDescriptor<grpc.smart_hub.StatusRequest, grpc.smart_hub.StatusResponse> getStatusUpdateMethod;
    if ((getStatusUpdateMethod = SmartHubServiceGrpc.getStatusUpdateMethod) == null) {
      synchronized (SmartHubServiceGrpc.class) {
        if ((getStatusUpdateMethod = SmartHubServiceGrpc.getStatusUpdateMethod) == null) {
          SmartHubServiceGrpc.getStatusUpdateMethod = getStatusUpdateMethod = 
              io.grpc.MethodDescriptor.<grpc.smart_hub.StatusRequest, grpc.smart_hub.StatusResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "smart_hub.SmartHubService", "statusUpdate"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.smart_hub.StatusRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.smart_hub.StatusResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new SmartHubServiceMethodDescriptorSupplier("statusUpdate"))
                  .build();
          }
        }
     }
     return getStatusUpdateMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      grpc.smart_hub.StatusResponse> getGetAllStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getAllStatus",
      requestType = com.google.protobuf.Empty.class,
      responseType = grpc.smart_hub.StatusResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      grpc.smart_hub.StatusResponse> getGetAllStatusMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, grpc.smart_hub.StatusResponse> getGetAllStatusMethod;
    if ((getGetAllStatusMethod = SmartHubServiceGrpc.getGetAllStatusMethod) == null) {
      synchronized (SmartHubServiceGrpc.class) {
        if ((getGetAllStatusMethod = SmartHubServiceGrpc.getGetAllStatusMethod) == null) {
          SmartHubServiceGrpc.getGetAllStatusMethod = getGetAllStatusMethod = 
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, grpc.smart_hub.StatusResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "smart_hub.SmartHubService", "getAllStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.smart_hub.StatusResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new SmartHubServiceMethodDescriptorSupplier("getAllStatus"))
                  .build();
          }
        }
     }
     return getGetAllStatusMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SmartHubServiceStub newStub(io.grpc.Channel channel) {
    return new SmartHubServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SmartHubServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new SmartHubServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SmartHubServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new SmartHubServiceFutureStub(channel);
  }

  /**
   * <pre>
   * The SmartHubService manages user food requests.
   * </pre>
   */
  public static abstract class SmartHubServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void handleFoodRequests(grpc.common.FoodRequest request,
        io.grpc.stub.StreamObserver<grpc.smart_hub.StatusResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getHandleFoodRequestsMethod(), responseObserver);
    }

    /**
     */
    public void statusUpdate(grpc.smart_hub.StatusRequest request,
        io.grpc.stub.StreamObserver<grpc.smart_hub.StatusResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getStatusUpdateMethod(), responseObserver);
    }

    /**
     * <pre>
     * No parameters are needed for this method so we use the default
     * google.protobuf.Empty to show this
     * </pre>
     */
    public void getAllStatus(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<grpc.smart_hub.StatusResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetAllStatusMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getHandleFoodRequestsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpc.common.FoodRequest,
                grpc.smart_hub.StatusResponse>(
                  this, METHODID_HANDLE_FOOD_REQUESTS)))
          .addMethod(
            getStatusUpdateMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpc.smart_hub.StatusRequest,
                grpc.smart_hub.StatusResponse>(
                  this, METHODID_STATUS_UPDATE)))
          .addMethod(
            getGetAllStatusMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                grpc.smart_hub.StatusResponse>(
                  this, METHODID_GET_ALL_STATUS)))
          .build();
    }
  }

  /**
   * <pre>
   * The SmartHubService manages user food requests.
   * </pre>
   */
  public static final class SmartHubServiceStub extends io.grpc.stub.AbstractStub<SmartHubServiceStub> {
    private SmartHubServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SmartHubServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SmartHubServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SmartHubServiceStub(channel, callOptions);
    }

    /**
     */
    public void handleFoodRequests(grpc.common.FoodRequest request,
        io.grpc.stub.StreamObserver<grpc.smart_hub.StatusResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getHandleFoodRequestsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void statusUpdate(grpc.smart_hub.StatusRequest request,
        io.grpc.stub.StreamObserver<grpc.smart_hub.StatusResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getStatusUpdateMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * No parameters are needed for this method so we use the default
     * google.protobuf.Empty to show this
     * </pre>
     */
    public void getAllStatus(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<grpc.smart_hub.StatusResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getGetAllStatusMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * The SmartHubService manages user food requests.
   * </pre>
   */
  public static final class SmartHubServiceBlockingStub extends io.grpc.stub.AbstractStub<SmartHubServiceBlockingStub> {
    private SmartHubServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SmartHubServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SmartHubServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SmartHubServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public grpc.smart_hub.StatusResponse handleFoodRequests(grpc.common.FoodRequest request) {
      return blockingUnaryCall(
          getChannel(), getHandleFoodRequestsMethod(), getCallOptions(), request);
    }

    /**
     */
    public grpc.smart_hub.StatusResponse statusUpdate(grpc.smart_hub.StatusRequest request) {
      return blockingUnaryCall(
          getChannel(), getStatusUpdateMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * No parameters are needed for this method so we use the default
     * google.protobuf.Empty to show this
     * </pre>
     */
    public java.util.Iterator<grpc.smart_hub.StatusResponse> getAllStatus(
        com.google.protobuf.Empty request) {
      return blockingServerStreamingCall(
          getChannel(), getGetAllStatusMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * The SmartHubService manages user food requests.
   * </pre>
   */
  public static final class SmartHubServiceFutureStub extends io.grpc.stub.AbstractStub<SmartHubServiceFutureStub> {
    private SmartHubServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SmartHubServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SmartHubServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SmartHubServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.smart_hub.StatusResponse> handleFoodRequests(
        grpc.common.FoodRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getHandleFoodRequestsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.smart_hub.StatusResponse> statusUpdate(
        grpc.smart_hub.StatusRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getStatusUpdateMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_HANDLE_FOOD_REQUESTS = 0;
  private static final int METHODID_STATUS_UPDATE = 1;
  private static final int METHODID_GET_ALL_STATUS = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final SmartHubServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(SmartHubServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_HANDLE_FOOD_REQUESTS:
          serviceImpl.handleFoodRequests((grpc.common.FoodRequest) request,
              (io.grpc.stub.StreamObserver<grpc.smart_hub.StatusResponse>) responseObserver);
          break;
        case METHODID_STATUS_UPDATE:
          serviceImpl.statusUpdate((grpc.smart_hub.StatusRequest) request,
              (io.grpc.stub.StreamObserver<grpc.smart_hub.StatusResponse>) responseObserver);
          break;
        case METHODID_GET_ALL_STATUS:
          serviceImpl.getAllStatus((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<grpc.smart_hub.StatusResponse>) responseObserver);
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

  private static abstract class SmartHubServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SmartHubServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpc.smart_hub.SmartHub.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SmartHubService");
    }
  }

  private static final class SmartHubServiceFileDescriptorSupplier
      extends SmartHubServiceBaseDescriptorSupplier {
    SmartHubServiceFileDescriptorSupplier() {}
  }

  private static final class SmartHubServiceMethodDescriptorSupplier
      extends SmartHubServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    SmartHubServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (SmartHubServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SmartHubServiceFileDescriptorSupplier())
              .addMethod(getHandleFoodRequestsMethod())
              .addMethod(getStatusUpdateMethod())
              .addMethod(getGetAllStatusMethod())
              .build();
        }
      }
    }
    return result;
  }
}
