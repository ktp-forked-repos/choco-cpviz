/**
 * This file is part of choco-cpviz, https://github.com/chocoteam/choco-cpviz
 *
 * Copyright (c) 2017-09-08T13:48:05Z, IMT Atlantique. All rights reserved.
 *
 * Licensed under the BSD 4-clause license.
 * See LICENSE file in the project root for full license information.
 */
package org.chocosolver.samples; /**
 *  Copyright (c) 1999-2011, Ecole des Mines de Nantes
 *  All rights reserved.
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the Ecole des Mines de Nantes nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
 *  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import org.chocosolver.cpviz.Visualization;
import org.chocosolver.cpviz.visualizers.Vector;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import org.slf4j.LoggerFactory;


/**
 * <b>The famous SEND + MORE = MONEY problem.</b></br>
 * The Send More Money Problem consists in finding distinct digits for the letters D, E, M, N, O, R, S, Y
 * such that S and M are different from zero (no leading zeros) and the equation SEND + MORE = MONEY is satisfied.
 *
 * @version 2.0.1</br>
 */
public class SendMoreMoney{

    Model model;
    Solver solver;

    IntVar S, E, N, D, M, O, R, Y;
    IntVar[] ALL;

    public void createSolver() {
        model = new Model("SendMoreMoney");
        solver = model.getSolver();
    }

    public void buildModel() {
        S = model.intVar("S", 0, 9, false);
        E = model.intVar("E", 0, 9, false);
        N = model.intVar("N", 0, 9, false);
        D = model.intVar("D", 0, 9, false);
        M = model.intVar("M", 0, 9, false);
        O = model.intVar("0", 0, 9, false);
        R = model.intVar("R", 0, 9, false);
        Y = model.intVar("Y", 0, 9, false);

        model.post(model.arithm(S, "!=", 0));
        model.post(model.arithm(M, "!=", 0));
        model.post(model.allDifferent(new IntVar[]{S, E, N, D, M, O, R, Y}, "BC"));


        ALL = new IntVar[]{
                S, E, N, D,
                M, O, R, E,
                M, O, N, E, Y};
        int[] COEFFS = new int[]{
                1000, 100, 10, 1,
                1000, 100, 10, 1,
                -10000, -1000, -100, -10, -1
        };
        model.post(model.scalar(ALL, COEFFS, "=", model.intVar(0)));
    }

    public void configureSearch() {
    }

    public void solve() {
        //-------> Visualization declaration starts here <-------//
        // create a new instance of Visualization
        Visualization visu = new Visualization("SendMoreMoney", model.getSolver(), System.getProperty("user.dir"));
        visu.createTree(); // declare tree search visualization
        visu.createViz(); // declare visualizers container
        // create a new Vector visualizer
        Vector visualizer = new Vector(new IntVar[]{S, E, N, D, M, O, R, Y},
                "expanded", 0, 0, 8, 10, "SENDMORY", 0, 9);
        // add the vector to the visualizers container
        visu.addVisualizer(visualizer);
        solver.solve();
    }

    public void prettyOut() {
        LoggerFactory.getLogger("bench").info("SEND + MORE = MONEY ");
        StringBuilder st = new StringBuilder();
        st.append("\t");
        for (int i = 0; i < ALL.length; i++) {
            st.append(String.format("%s : %d\n\t", ALL[i].getName(), ALL[i].getValue()));
        }
        LoggerFactory.getLogger("bench").info(st.toString());
    }

    public static void main(String[] args) {
        SendMoreMoney sendMoreMoney = new SendMoreMoney();
        sendMoreMoney.createSolver();
        sendMoreMoney.buildModel();
        sendMoreMoney.configureSearch();
        sendMoreMoney.solve();
        sendMoreMoney.prettyOut();
    }

}
