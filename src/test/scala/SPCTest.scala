package amba

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import amba.Constants._

class SPCTest extends AnyFlatSpec with ChiselScalatestTester {
  "SPC" should "pass" in {
    test(new SPC) { dut =>
      dut.io.req.poke(HIGH)
      dut.io.state.poke(2.U)
      dut.clock.step()
      dut.clock.step()
      dut.clock.step()
      dut.clock.step()
    }
  }
}
