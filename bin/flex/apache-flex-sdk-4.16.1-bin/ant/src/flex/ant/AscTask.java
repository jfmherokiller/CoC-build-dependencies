/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package flex.ant;

import flex.ant.config.ConfigVariable;
import flex.ant.config.ConfigBoolean;
import flex.ant.config.ConfigString;
import flex.ant.config.OptionSpec;
import flex.ant.config.OptionSource;
import flex.ant.types.FlexFileSet;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicAttribute;
import org.apache.tools.ant.types.Commandline;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Implements the &lt;asc&gt; Ant task. 
 */
public final class AscTask extends FlexTask implements DynamicAttribute
{

    /*=======================================================================*
     *  Static variables and initializer                                     *
     *=======================================================================*/

    private static ConfigVariable[] ASC_VARIABLES;

    private static OptionSpec imSpec = new OptionSpec("import");
    private static OptionSpec inSpec = new OptionSpec("in");
    private static OptionSpec swSpec = new OptionSpec("swf");

    static
    {
        ASC_VARIABLES = new ConfigVariable[] {
            //Boolean Variables
            new ConfigBoolean(new OptionSpec("as3")),
            new ConfigBoolean(new OptionSpec("es")),
            new ConfigBoolean(new OptionSpec("d")),
            new ConfigBoolean(new OptionSpec("f")),
            new ConfigBoolean(new OptionSpec("i")),
            new ConfigBoolean(new OptionSpec("m")),
            new ConfigBoolean(new OptionSpec("n")),
            new ConfigBoolean(new OptionSpec("md")),
            new ConfigBoolean(new OptionSpec("warnings")),
            new ConfigBoolean(new OptionSpec("strict")),
            new ConfigBoolean(new OptionSpec("sanity")),
            new ConfigBoolean(new OptionSpec("optimize")),
            //String Variables
            new ConfigString(new OptionSpec("log")),
            new ConfigString(new OptionSpec("exe")),
            new ConfigString(new OptionSpec("language")),
        };
    }

    /*=======================================================================*
     *
     *=======================================================================*/

    private final ArrayList<FlexFileSet> nestedFileSets;

    private FlexFileSet fileSpec;
    private Swf swf;

    /**
     *
     */
    public AscTask()
    {
        super("asc", "macromedia.asc.embedding.Main", "asc.jar", ASC_VARIABLES);

        nestedFileSets = new ArrayList<FlexFileSet>();
    }

    /*=======================================================================*
     *  Child Elements                                                       *
     *=======================================================================*/

    public FlexFileSet createImport()
    {
        return createFileSet(imSpec);
    }

    public FlexFileSet createIn()
    {
        return createFileSet(inSpec);
    }

    public FlexFileSet createFilespec()
    {
        if (fileSpec == null)
            return fileSpec = new FlexFileSet();
        else
            throw new BuildException("Only one nested <filespec> element is allowed in an <asc> task.");
    }

    public Swf createSwf()
    {
        if (swf == null)
            return swf = new Swf();
        else 
            throw new BuildException("Only one nested <swf> element is allowed in an <asc> task.");
    }

    private FlexFileSet createFileSet(OptionSpec spec)
    {
        FlexFileSet e = new FlexFileSet(spec);
        nestedFileSets.add(e);
        return e;
    }

    /*=======================================================================*
     *  Execute and Related Functions                                        *
     *=======================================================================*/

    protected void prepareCommandline()
    {
        for (int i = 0; i < variables.length; i++) {
            variables[i].addToCommandline(cmdl);
        }

        if (swf != null)
            swf.addToCommandline(cmdl);

        Iterator<FlexFileSet> it = nestedFileSets.iterator();

        while (it.hasNext()) {
            it.next().addToCommandline(cmdl);
        }

        fileSpec.addToCommandline(cmdl);
    }

    /*=======================================================================*
     *  Inner Classes                                                        *
     *=======================================================================*/

    protected class Swf implements OptionSource
    {
        private String classname = null;
        private String width = null;
        private String height = null;

        private int fps = -1;
        private boolean fpsSet = false;

        public Swf() { }

        public void setClassname(String value)
        {
            classname = value;
        }

        public void setWidth(String value)
        {
            width = value;
        }

        public void setHeight(String value)
        {
            height = value;
        }

        public void setFps(int value)
        {
            fps = value;
            fpsSet = true;
        }

        public void addToCommandline(Commandline cmdl)
        {
            cmdl.createArgument().setValue("-" + swSpec.getFullName());
            cmdl.createArgument().setValue(classname + "," + width 
                                           + "," + height 
                                           + (fpsSet ? "," + String.valueOf(fps) : ""));
        }

    } //End of Swf

} //End of AscTask
