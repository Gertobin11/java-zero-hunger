// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: smart_hub.proto

package grpc.smart_hub;

public final class SmartHub {
  private SmartHub() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_smart_hub_StatusRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_smart_hub_StatusRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_smart_hub_StatusResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_smart_hub_StatusResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\017smart_hub.proto\022\tsmart_hub\032\014common.pro" +
      "to\032\033google/protobuf/empty.proto\"#\n\rStatu" +
      "sRequest\022\022\n\nrequest_id\030\001 \001(\005\"J\n\016StatusRe" +
      "sponse\022\016\n\006status\030\001 \001(\t\022\023\n\013delivery_id\030\002 " +
      "\001(\005\022\023\n\013pickup_time\030\003 \001(\t2\242\002\n\017SmartHubSer" +
      "vice\022D\n\022handleFoodRequests\022\023.common.Food" +
      "Request\032\031.smart_hub.StatusResponse\022C\n\014st" +
      "atusUpdate\022\030.smart_hub.StatusRequest\032\031.s" +
      "mart_hub.StatusResponse\022C\n\014getAllStatus\022" +
      "\026.google.protobuf.Empty\032\031.smart_hub.Stat" +
      "usResponse0\001\022?\n\rtriggerChecks\022\026.google.p" +
      "rotobuf.Empty\032\026.google.protobuf.EmptyB\022\n" +
      "\016grpc.smart_hubP\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          grpc.common.Common.getDescriptor(),
          com.google.protobuf.EmptyProto.getDescriptor(),
        }, assigner);
    internal_static_smart_hub_StatusRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_smart_hub_StatusRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_smart_hub_StatusRequest_descriptor,
        new java.lang.String[] { "RequestId", });
    internal_static_smart_hub_StatusResponse_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_smart_hub_StatusResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_smart_hub_StatusResponse_descriptor,
        new java.lang.String[] { "Status", "DeliveryId", "PickupTime", });
    grpc.common.Common.getDescriptor();
    com.google.protobuf.EmptyProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
