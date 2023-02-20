package amba

import chisel3._
import chisel3.util._

object Constants {
  val N = 4 // PACTIVE bits
  val M = 4 // STATE bits

  val HIGH = true.B
  val LOW = false.B

  val p_STABLE :: p_RESET :: p_REQUEST :: p_ACCEPT :: p_COMPLETE :: p_DENIED :: p_CONTINUE :: Nil =
    Enum(7)
}
