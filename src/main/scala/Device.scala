package amba

import chisel3._
import chisel3.util._
import amba.Constants._

class Device extends Module {
  val io = IO(new Bundle {
    val PACTIVE = Output(UInt(N.W))
    val PSTATE = Input(UInt(M.W))
    val PREQ = Input(Bool())
    val PACCEPT = Output(Bool())
    val PDENY = Output(Bool())
    val RESETn = Input(Bool())
  })

  val STATE = RegInit(p_STABLE)
  val PSTATE = RegInit(0.U(M.W))
  val PACCEPT = RegInit(LOW)
  val PDENY = RegInit(LOW)
  val PACTIVE = RegInit(0.U(N.W))

  switch(STATE) {
    is(p_STABLE) {
      when(io.PREQ) {
        PACCEPT := HIGH
        PSTATE := io.PSTATE
        // PDENY := HIGH
        STATE := p_ACCEPT
      }
    }

    is(p_ACCEPT) {
      when(io.PREQ === LOW) {
        PACCEPT := LOW
        // PDENY := LOW
        STATE := p_STABLE
      }
    }
  }

  io.PACCEPT := PACCEPT
  io.PDENY := PDENY
  io.PACTIVE := PACTIVE
}
