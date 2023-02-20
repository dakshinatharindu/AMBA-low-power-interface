rm -r obj_dir
verilator -Wall --trace --cc ../verilog/SPC.v --exe SPC.cpp &>-
make -C obj_dir -f VSPC.mk &>-
obj_dir/VSPC
gtkwave waveform.vcd