package amba

import chisel3._
import chisel3.util._
import amba.Constants._
import Chisel.OUTPUT

class Controller extends Module {
  val io = IO(new Bundle {
    val PACTIVE = Input(UInt(N.W))
    val PSTATE = Output(UInt(M.W))
    val PREQ = Output(Bool())
    val PACCEPT = Input(Bool())
    val PDENY = Input(Bool())
    val RESETn = Output(Bool())
  })

  val STATE = RegInit(p_RESET)

}
