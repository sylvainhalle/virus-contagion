/*
    A BeepBeep simulation of coronavirus contagion
    Copyright (C) 2020 Sylvain Hallé and friends

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Handling of ball kinetics. Most of the code in this package is adapted
 * from an answer on <a href="https://stackoverflow.com/q/345838">StackOverflow</a>.
 * It is not intended to be extremely precise nor fast, but it does the job
 * pretty well of calculating elastic collisions for the purpose of this small
 * example.
 */
package virussim.physics;