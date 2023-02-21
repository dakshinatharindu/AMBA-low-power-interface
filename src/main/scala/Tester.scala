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
      assert(PACCEPT === LOW, "PACCEPT is HIGH at STABLE state")
      assert(PDENY === LOW, "PDENY is HIGH at STABLE state")

      when(PREQ){
        STATE := p_REQUEST
        CURRENT_PSTATE := PSTATE
        PREV_PSTATE := CURRENT_PSTATE
      }
    }

    is (p_REQUEST){
        assert(PREQ === HIGH, "PREQ should be kept HIGH at REQUEST state")
        when(PACCEPT){
            STATE := p_ACCEPT
        }.elsewhen(PDENY){
            STATE := p_DENIED
        }
    }

    is (p_ACCEPT){
        assert(PACCEPT === HIGH, "PACCEPT should be kept HIGH at ACCEPT state")
        assert(PDENY === LOW, "PDENY should be kept LOW at ACCEPT state")
        when(PREQ === LOW){
            STATE := p_COMPLETE
        }
    }

    is (p_DENIED){
        assert(PACCEPT === LOW, "PACCEPT should be kept LOW at DENIED state")
        assert(PDENY === HIGH, "PDENY should be kept HIGH at DENIED state")
        when(PREQ === LOW){
            STATE := p_CONTINUE
        }
    }
  }
}
