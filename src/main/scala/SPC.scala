package amba

import chisel3._
import chisel3.util._
import chisel3.stage._
import amba.Constants._

class SPC extends Module {
  val io = IO(new Bundle {
    val req = Input(Bool())
    val state = Input(UInt(M.W))
  })

  val controller = Module(new Controller)
  val device = Module(new Device)
  val tester = Module(new Tester)

  controller.io.req := io.req
  controller.io.state := io.state
  controller.io.PACTIVE := device.io.PACTIVE
  controller.io.PACCEPT := device.io.PACCEPT
  controller.io.PDENY := device.io.PDENY

  device.io.PSTATE := controller.io.PSTATE
  device.io.PREQ := controller.io.PREQ
  device.io.RESETn := controller.io.RESETn

  tester.io.PACTIVE := device.io.PACTIVE
  tester.io.PACCEPT := device.io.PACCEPT
  tester.io.PDENY := device.io.PDENY
  tester.io.PSTATE := controller.io.PSTATE
  tester.io.PREQ := controller.io.PREQ
  tester.io.RESETn := controller.io.RESETn

}

object SPC extends App {
  val myverilog = (new ChiselStage).emitVerilog(
    new SPC,
    Array("--target-dir", "verilog/")
  )
}
