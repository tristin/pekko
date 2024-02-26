/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * license agreements; and to You under the Apache License, version 2.0:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * This file is part of the Apache Pekko project, which was derived from Akka.
 */

/*
 * Copyright (C) 2009-2022 Lightbend Inc. <https://www.lightbend.com>
 */

package org.apache.pekko.remote

import java.util.concurrent.TimeUnit.NANOSECONDS

/**
 * A failure detector must be a thread-safe mutable construct that registers heartbeat events of a resource and is able to
 * decide the availability of that monitored resource.
 */
trait FailureDetector {

  /**
   * Returns true if the resource is considered to be up and healthy and returns false otherwise.
   */
  def isAvailable: Boolean

  /**
   * Returns true if the failure detector has received any heartbeats and started monitoring
   * of the resource.
   */
  def isMonitoring: Boolean

  /**
   * Notifies the FailureDetector that a heartbeat arrived from the monitored resource. This causes the FailureDetector
   * to update its state.
   */
  def heartbeat(): Unit

}

trait FailureDetectorWithAddress {

  /**
   * Address of observed host will be set after detector creation.
   */
  def setAddress(addr: String): Unit
}

object FailureDetector {

  /**
   * Abstraction of a clock that returns time in milliseconds. Clock can only be used to measure elapsed
   * time and is not related to any other notion of system or wall-clock time.
   */
  // Abstract class to be able to extend it from Java
  abstract class Clock extends (() => Long)

  implicit val defaultClock: Clock = new Clock {
    def apply(): Long = NANOSECONDS.toMillis(System.nanoTime)
  }
}
