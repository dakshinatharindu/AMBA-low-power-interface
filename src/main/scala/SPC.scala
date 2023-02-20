package amba

import chisel3._
import chisel3.util._
import chisel3.stage._
import amba.Constants._

class SPC extends Module {
  val io = IO(new Bundle {
    val PACTIVE = Output(UInt(N.W))
    val PSTATE = Output(UInt(M.W))
    val PREQ = Output(Bool())
    val PACCEPT = Output(Bool())
    val PDENY = Output(Bool())
    val RESETn = Output(Bool())

    val req = Input(Bool())
    val state = Input(UInt(M.W))
  })

  val controller = Module(new Controller)
  val device = Module (new Device)

  controller.io.req := io.req
  controller.io.state := io.state
  controller.io.PACTIVE := device.io.PACTIVE
  controller.io.PACCEPT := device.io.PACCEPT
  controller.io.PDENY := device.io.PDENY

  device.io.PSTATE := controller.io.PSTATE
  device.io.PREQ := controller.io.PREQ
  device.io.RESETn := controller.io.RESETn

  io.PACTIVE := device.io.PACTIVE
  io.PACCEPT := device.io.PACCEPT
  io.PDENY := device.io.PDENY
  io.PSTATE := controller.io.PSTATE
  io.PREQ := controller.io.PREQ
  io.RESETn := controller.io.RESETn

}

object SPC extends App {
  val myverilog = (new ChiselStage).emitVerilog(
    new SPC,
    Array("--target-dir", "verilog/")
  )
}

