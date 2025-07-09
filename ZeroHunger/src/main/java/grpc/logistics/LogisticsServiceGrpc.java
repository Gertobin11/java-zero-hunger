package grpc.logistics;

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
 * The LogisticsService manages the physical delivery of food items.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: logistics.proto")
public final class LogisticsServiceGrpc {

  private LogisticsServiceGrpc() {}

  public static final String SERVICE_NAME = "logistics.LogisticsService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<grpc.logistics.Delivery,
      grpc.logistics.DeliveryResponse> getHandleDeliveryRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "HandleDeliveryRequest",
      requestType = grpc.logistics.Delivery.class,
      responseType = grpc.logistics.DeliveryResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc.logistics.Delivery,
      grpc.logistics.DeliveryResponse> getHandleDeliveryRequestMethod() {
    io.grpc.MethodDescriptor<grpc.logistics.Delivery, grpc.logistics.DeliveryResponse> getHandleDeliveryRequestMethod;
    if ((getHandleDeliveryRequestMethod = LogisticsServiceGrpc.getHandleDeliveryRequestMethod) == null) {
      synchronized (LogisticsServiceGrpc.class) {
        if ((getHandleDeliveryRequestMethod = LogisticsServiceGrpc.getHandleDeliveryRequestMethod) == null) {
          LogisticsServiceGrpc.getHandleDeliveryRequestMethod = getHandleDeliveryRequestMethod = 
              io.grpc.MethodDescriptor.<grpc.logistics.Delivery, grpc.logistics.DeliveryResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "logistics.LogisticsService", "HandleDeliveryRequest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.logistics.Delivery.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.logistics.DeliveryResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new LogisticsServiceMethodDescriptorSupplier("HandleDeliveryRequest"))
                  .build();
          }
        }
     }
     return getHandleDeliveryRequestMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpc.logistics.LocationUpdate,
      grpc.logistics.Location> getTrackDeliveryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "TrackDelivery",
      requestType = grpc.logistics.LocationUpdate.class,
      responseType = grpc.logistics.Location.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<grpc.logistics.LocationUpdate,
      grpc.logistics.Location> getTrackDeliveryMethod() {
    io.grpc.MethodDescriptor<grpc.logistics.LocationUpdate, grpc.logistics.Location> getTrackDeliveryMethod;
    if ((getTrackDeliveryMethod = LogisticsServiceGrpc.getTrackDeliveryMethod) == null) {
      synchronized (LogisticsServiceGrpc.class) {
        if ((getTrackDeliveryMethod = LogisticsServiceGrpc.getTrackDeliveryMethod) == null) {
          LogisticsServiceGrpc.getTrackDeliveryMethod = getTrackDeliveryMethod = 
              io.grpc.MethodDescriptor.<grpc.logistics.LocationUpdate, grpc.logistics.Location>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "logistics.LogisticsService", "TrackDelivery"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.logistics.LocationUpdate.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.logistics.Location.getDefaultInstance()))
                  .setSchemaDescriptor(new LogisticsServiceMethodDescriptorSupplier("TrackDelivery"))
                  .build();
          }
        }
     }
     return getTrackDeliveryMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static LogisticsServiceStub newStub(io.grpc.Channel channel) {
    return new LogisticsServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static LogisticsServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new LogisticsServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static LogisticsServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new LogisticsServiceFutureStub(channel);
  }

  /**
   * <pre>
   * The LogisticsService manages the physical delivery of food items.
   * </pre>
   */
  public static abstract class LogisticsServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void handleDeliveryRequest(grpc.logistics.Delivery request,
        io.grpc.stub.StreamObserver<grpc.logistics.DeliveryResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getHandleDeliveryRequestMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<grpc.logistics.LocationUpdate> trackDelivery(
        io.grpc.stub.StreamObserver<grpc.logistics.Location> responseObserver) {
      return asyncUnimplementedStreamingCall(getTrackDeliveryMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getHandleDeliveryRequestMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpc.logistics.Delivery,
                grpc.logistics.DeliveryResponse>(
                  this, METHODID_HANDLE_DELIVERY_REQUEST)))
          .addMethod(
            getTrackDeliveryMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                grpc.logistics.LocationUpdate,
                grpc.logistics.Location>(
                  this, METHODID_TRACK_DELIVERY)))
          .build();
    }
  }

  /**
   * <pre>
   * The LogisticsService manages the physical delivery of food items.
   * </pre>
   */
  public static final class LogisticsServiceStub extends io.grpc.stub.AbstractStub<LogisticsServiceStub> {
    private LogisticsServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private LogisticsServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LogisticsServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new LogisticsServiceStub(channel, callOptions);
    }

    /**
     */
    public void handleDeliveryRequest(grpc.logistics.Delivery request,
        io.grpc.stub.StreamObserver<grpc.logistics.DeliveryResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getHandleDeliveryRequestMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<grpc.logistics.LocationUpdate> trackDelivery(
        io.grpc.stub.StreamObserver<grpc.logistics.Location> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getTrackDeliveryMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * <pre>
   * The LogisticsService manages the physical delivery of food items.
   * </pre>
   */
  public static final class LogisticsServiceBlockingStub extends io.grpc.stub.AbstractStub<LogisticsServiceBlockingStub> {
    private LogisticsServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private LogisticsServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LogisticsServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new LogisticsServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public grpc.logistics.DeliveryResponse handleDeliveryRequest(grpc.logistics.Delivery request) {
      return blockingUnaryCall(
          getChannel(), getHandleDeliveryRequestMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * The LogisticsService manages the physical delivery of food items.
   * </pre>
   */
  public static final class LogisticsServiceFutureStub extends io.grpc.stub.AbstractStub<LogisticsServiceFutureStub> {
    private LogisticsServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private LogisticsServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LogisticsServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new LogisticsServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.logistics.DeliveryResponse> handleDeliveryRequest(
        grpc.logistics.Delivery request) {
      return futureUnaryCall(
          getChannel().newCall(getHandleDeliveryRequestMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_HANDLE_DELIVERY_REQUEST = 0;
  private static final int METHODID_TRACK_DELIVERY = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final LogisticsServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(LogisticsServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_HANDLE_DELIVERY_REQUEST:
          serviceImpl.handleDeliveryRequest((grpc.logistics.Delivery) request,
              (io.grpc.stub.StreamObserver<grpc.logistics.DeliveryResponse>) responseObserver);
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
        case METHODID_TRACK_DELIVERY:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.trackDelivery(
              (io.grpc.stub.StreamObserver<grpc.logistics.Location>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class LogisticsServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    LogisticsServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpc.logistics.Logistics.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("LogisticsService");
    }
  }

  private static final class LogisticsServiceFileDescriptorSupplier
      extends LogisticsServiceBaseDescriptorSupplier {
    LogisticsServiceFileDescriptorSupplier() {}
  }

  private static final class LogisticsServiceMethodDescriptorSupplier
      extends LogisticsServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    LogisticsServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (LogisticsServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new LogisticsServiceFileDescriptorSupplier())
              .addMethod(getHandleDeliveryRequestMethod())
              .addMethod(getTrackDeliveryMethod())
              .build();
        }
      }
    }
    return result;
  }
}
