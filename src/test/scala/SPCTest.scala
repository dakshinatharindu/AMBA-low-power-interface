package amba

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import amba.Constants._

class SPCTest extends AnyFlatSpec with ChiselScalatestTester {
  "SPC" should "pass" in {
    test(new SPC) { dut =>
      dut.io.rst.poke(HIGH)
      dut.clock.step()
      dut.clock.step()
      dut.io.rst.poke(LOW)
      dut.clock.step()
      dut.clock.step()
      dut.io.req.poke(HIGH)
      dut.io.state.poke(1.U)
      dut.clock.step()
      dut.io.rst.poke(HIGH)
      dut.clock.step()
      dut.clock.step()
      dut.clock.step()
      dut.clock.step()
    }
  }
}
