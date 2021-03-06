/*
 * Copyright (C) 2019 Qiufeng54321
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/* 
 * File:   LibLoader.hpp
 * Author: Qiufeng54321
 *
 * Created on 2019年5月18日, 下午4:16
 */
#pragma once
#ifndef LIBLOADER_HPP
#define LIBLOADER_HPP

#include "include/dynamicro.h"

namespace Erable::Native::Library {
    class Loader {
    public:
        Dynamicro *dyn;

        Loader();

        void load(const std::string &path);
    };
}

#endif /* LIBLOADER_HPP */

