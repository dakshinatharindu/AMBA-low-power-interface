#include <VSPC.h>
#include <stdlib.h>
#include <verilated.h>
#include <verilated_vcd_c.h>

#include <iostream>

vluint64_t sim_time = 0;
#define MAX_SIM_TIME 20

int main(int argc, char** argv, char** env) {
    VerilatedContext* contextp = new VerilatedContext;
    contextp->commandArgs(argc, argv);
    VSPC* dut = new VSPC{contextp};

    Verilated::traceEverOn(true);
    VerilatedVcdC* m_trace = new VerilatedVcdC;
    dut->trace(m_trace, 5);
    m_trace->open("waveform.vcd");
    dut->io_req = 1;
    dut->io_state = 2;
    while (sim_time < MAX_SIM_TIME) {
        dut->clock ^= 1;
        dut->eval();
        m_trace->dump(sim_time);
        sim_time++;
    }
    printf("DONE!\n");
    m_trace->close();
    delete dut;
    exit(EXIT_SUCCESS);
}