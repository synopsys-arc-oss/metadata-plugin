/*
 *  The MIT License
 *
 *  Copyright 2011 Sony Ericsson Mobile Communications. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package com.sonyericsson.hudson.plugins.metadata.contributors;

import com.sonyericsson.hudson.plugins.metadata.Messages;
import com.sonyericsson.hudson.plugins.metadata.model.MetaDataBuildAction;
import com.sonyericsson.hudson.plugins.metadata.model.values.AbstractMetaDataValue;
import com.sonyericsson.hudson.plugins.metadata.model.values.TreeStructureUtil;
import hudson.Extension;
import hudson.ExtensionList;
import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * The controller for {@link BuildMetaDataContributor}s.
 *
 * @author Robert Sandell &lt;robert.sandell@sonyericsson.com&gt;
 */
@Extension
public class BuildContributorsController extends RunListener<AbstractBuild> {

    private static final Logger logger = Logger.getLogger(BuildContributorsController.class.getName());

    @Override
    public void onCompleted(AbstractBuild build, TaskListener listener) {
        logger.entering(BuildContributorsController.class.getName(), "onCompleted({0})", build);
        MetaDataBuildAction action = build.getAction(MetaDataBuildAction.class);
        if (action == null) {
            action = new MetaDataBuildAction();
            build.addAction(action);
        }
        listener.getLogger().println(Messages.BuildContributorsController_LogMessage_Collecting());
        logger.finest("Starting collection.");
        ExtensionList<BuildMetaDataContributor> contributors = BuildMetaDataContributor.all();
        for (BuildMetaDataContributor contributor : contributors) {
            List<AbstractMetaDataValue> values = contributor.getMetaDataFor(build);
            Collection<AbstractMetaDataValue> leftovers = action.addChildValues(values);
            if (leftovers != null && !leftovers.isEmpty()) {
                String pretty = "\n" + TreeStructureUtil.prettyPrint(leftovers, "\t");
                listener.getLogger().println(
                        Messages.BuildContributorsController_LogMessage_LeftOvers(pretty));
                logger.warning("Some metadata failed to be merged for build " + build + pretty);
            }
        }
        listener.getLogger().println(Messages.BuildContributorsController_LogMessage_Done());
        logger.finest("Done collecting.");
    }
}