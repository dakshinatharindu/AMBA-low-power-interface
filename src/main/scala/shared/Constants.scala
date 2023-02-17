package amba

import chisel3._
import chisel3.util._

object Constants {
  val N = 4 // PACTIVE bits
  val M = 4 // STATE bits

  val p_RESET :: p_STABLE :: p_REQUEST :: p_ACCEPT :: p_COMPLETE :: p_DENIED :: p_CONTINUE :: Nil =
    Enum(7)
}

