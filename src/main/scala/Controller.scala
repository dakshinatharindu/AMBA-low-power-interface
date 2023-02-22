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

    val req = Input(Bool())
    val state = Input(UInt(M.W))
    val rst = Input(Bool())
  })

  val STATE = RegInit(p_STABLE)
  val PSTATE = RegInit(0.U(M.W))
  val PREV_PSTATE = RegInit(0.U(M.W))
  val PREQ = RegInit(LOW)
  val RESETn = RegInit(HIGH)

  switch(STATE) {
    is(p_STABLE) {
      when(io.req) {
        PREQ := HIGH
        PSTATE := io.state
        PREV_PSTATE := PSTATE
        STATE := p_REQUEST
      }
    }

    is(p_REQUEST) {
      when(io.PACCEPT) {
        PREQ := LOW
        STATE := p_COMPLETE
      }.elsewhen(io.PDENY) {
        PREQ := LOW
        PSTATE := PREV_PSTATE
        STATE := p_CONTINUE
      }
    }

    is(p_COMPLETE) {
      when(io.PACCEPT === LOW) {
        STATE := p_STABLE
      }
    }

    is(p_CONTINUE) {
      when(io.PDENY === LOW) {
        STATE := p_STABLE
      }
    }
  }

  io.PREQ := PREQ
  io.PSTATE := PSTATE
  io.RESETn := io.rst
}
