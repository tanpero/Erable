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

#include "NativeFunctions.hpp"

namespace Erable::Native {
    namespace BuiltIn {
/*
        DEFINE_NATIVE_FUNCTION(print) {
            Types::Instance *toP = argv->getAValue<Types::Array::arrtype>().at(0);
            std::cout << toP << std::endl;
            return nullptr;
        };

        DEFINE_NATIVE_FUNCTION(scan) {
            std::string get;
            std::cin >> get;
            Types::Instance *ret = new Types::String(get, self->getRetId(), desc);
            return ret;
        };

        DEFINE_NATIVE_FUNCTION(sqrt) {
            return nullptr;
        }

        DEFINE_NATIVE_FUNCTION(log) {
            return nullptr;
        }
        */
    }
/*
    void loadBuiltIn() {
        ADD_NATIVE(print, BuiltIn::print);
        ADD_NATIVE(scan, BuiltIn::scan);
        ADD_NATIVE(sqrt, BuiltIn::sqrt);
        ADD_NATIVE(log, BuiltIn::log);
    }
*/
}