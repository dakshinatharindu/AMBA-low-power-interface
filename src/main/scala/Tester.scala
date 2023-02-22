package amba

import chisel3._
import chisel3.util._
import chisel3.stage._
import amba.Constants._

class Tester extends Module {
  val io = IO(new Bundle {
    val PACTIVE = Input(UInt(N.W))
    val PSTATE = Input(UInt(M.W))
    val PREQ = Input(Bool())
    val PACCEPT = Input(Bool())
    val PDENY = Input(Bool())
    val RESETn = Input(Bool())
  })

  val PACTIVE = io.PACTIVE
  val PSTATE = io.PSTATE
  val PREQ = io.PREQ
  val PACCEPT = io.PACCEPT
  val PDENY = io.PDENY
  val RESETn = io.RESETn

  val STATE = RegInit(p_STABLE)
  val PREV_PSTATE = RegInit(0.U(M.W))
  val CURRENT_PSTATE = RegInit(0.U(M.W))

  switch(STATE) {
    is(p_STABLE) {
      when(RESETn === LOW) {
        assert(
          PACCEPT === LOW && PDENY === LOW,
          "PACCEPT and PDENY are LOW at RESETn falling edge"
        )
        STATE := p_RESET
      }.elsewhen(PREQ) {
        assert(
          PACCEPT === LOW && PDENY === LOW,
          "PACCEPT and PDENY are LOW at PREQ rising edge"
        )
        STATE := p_REQUEST
        CURRENT_PSTATE := PSTATE
        PREV_PSTATE := CURRENT_PSTATE
      }
    }

    is(p_REQUEST) {
      when(PACCEPT) {
        assert(PREQ === HIGH, "PREQ is HIGH at PACCEPT rising edge")
        assert(PDENY === LOW, "PDENY is LOW at PACCEPT rising edge")
        STATE := p_ACCEPT
      }.elsewhen(PDENY) {
        assert(PREQ === HIGH, "PREQ is HIGH at PDENY rising edge")
        assert(PACCEPT === LOW, "PACCEPT is LOW at PDENY rising edge")
        STATE := p_DENIED
      }
    }

    is(p_ACCEPT) {
      when(PREQ === LOW) {
        assert(PACCEPT === HIGH, "PACCEPT is HIGH at PREQ falling edge")
        assert(PDENY === LOW, "PDENY is LOW at PREQ falling edge")
        STATE := p_COMPLETE
      }
    }

    is(p_DENIED) {
      when(PREQ === LOW) {
        assert(PDENY === HIGH, "PDENY is HIGH at PREQ falling edge")
        assert(PACCEPT === LOW, "PACCEPT is LOW at PREQ falling edge")
        assert(PSTATE === PREV_PSTATE, "PSTATE is changed back to PREV_STATE")
        CURRENT_PSTATE := PREV_PSTATE
        STATE := p_CONTINUE
      }
    }

    is(p_COMPLETE) {
      when(PACCEPT === LOW) {
        assert(PREQ === LOW, "PREQ is LOW at PACCEPT falling edge")
        assert(PDENY === LOW, "PDENY is LOW at PACCEPT falling edge")
        STATE := p_STABLE
      }
    }

    is(p_CONTINUE) {
      when(PDENY === LOW) {
        assert(PREQ === LOW, "PREQ is LOW at PDENY falling edge")
        assert(PACCEPT === LOW, "PACCEPT is LOW at PDENY falling edge")
        STATE := p_STABLE
      }
    }

    is(p_RESET) {
      when(PREQ && RESETn) {
        assert(
          PACCEPT === LOW && PDENY === LOW,
          "PACCEPT and PDENY are LOW at RESETn rising edge"
        )
        STATE := p_REQUEST
        CURRENT_PSTATE := PSTATE
        PREV_PSTATE := CURRENT_PSTATE
      }.elsewhen(PREQ === LOW && RESETn) {
        assert(
          PACCEPT === LOW && PDENY === LOW,
          "PACCEPT and PDENY are LOW at RESETn rising edge"
        )
        STATE := p_STABLE
      }
    }
  }
}
