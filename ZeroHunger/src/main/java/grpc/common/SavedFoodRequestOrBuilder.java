// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: common.proto

package grpc.common;

public interface SavedFoodRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:common.SavedFoodRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int32 request_id = 1;</code>
   */
  int getRequestId();

  /**
   * <code>.common.FoodRequest foodRequest = 2;</code>
   */
  boolean hasFoodRequest();
  /**
   * <code>.common.FoodRequest foodRequest = 2;</code>
   */
  grpc.common.FoodRequest getFoodRequest();
  /**
   * <code>.common.FoodRequest foodRequest = 2;</code>
   */
  grpc.common.FoodRequestOrBuilder getFoodRequestOrBuilder();

  /**
   * <code>string status = 3;</code>
   */
  java.lang.String getStatus();
  /**
   * <code>string status = 3;</code>
   */
  com.google.protobuf.ByteString
      getStatusBytes();

  /**
   * <code>int32 delivery_id = 4;</code>
   */
  int getDeliveryId();

  /**
   * <code>string pickup_time = 5;</code>
   */
  java.lang.String getPickupTime();
  /**
   * <code>string pickup_time = 5;</code>
   */
  com.google.protobuf.ByteString
      getPickupTimeBytes();
}
