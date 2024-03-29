package amba

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import amba.Constants._

class ControllerTest extends AnyFlatSpec with ChiselScalatestTester {
  "Controller" should "pass" in {
    test(new Controller) { dut =>
      dut.io.req.poke(HIGH)
      dut.io.state.poke(2.U)
      dut.clock.step()
      dut.io.PDENY.poke(HIGH)
      dut.clock.step()
      println("PREQ : " + dut.io.PREQ.peek().toString())
      println("PSTATE : " + dut.io.PSTATE.peek().toString())
    }
  }
}
