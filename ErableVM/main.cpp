/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * File:   main.cpp
 * Author: Qiufeng54321
 *
 * Created on 2019年4月18日, 上午11:22
 */

#include <cstdlib>
#include "Descriptor.hpp"
//#include "Metadata.hpp"

/*
 *
 */
int main(int argc, char** argv) {
    Erable::Meta::Metadata md(new Erable::IO::InputStream(Erable::IO::File("../test/instance.ec")));
    md.readHeader();
    return 0;
}
