/*
 * The MIT License
 *
 * Copyright 2013 Oleg Nenashev <nenashev@synopsys.com>, Synopsys Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.sonyericsson.hudson.plugins.metadata.model;

import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Generic class for configuration of display options;
 * @author Oleg Nenashev <nenashev@synopsys.com>, Synopsys Inc.
 * @since 1.1.0
 */
public class MetadataDisplayOptions implements Describable<MetadataDisplayOptions> {
    private boolean displayedOnSummary;

    @DataBoundConstructor
    public MetadataDisplayOptions(boolean displayedOnSummary) {
        this.displayedOnSummary = displayedOnSummary;
    }
    
    /**
     * Enables displaying of value in the summary box.
     * @return true if value should be displayed on Summary
     * @since 1.1.0
     */
    boolean isDisplayedOnSummary() {
        return displayedOnSummary;
    }

    @Override
    public Descriptor<MetadataDisplayOptions> getDescriptor() {
        return DESCRIPTOR;
    }
      
    private static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();
    
    @Extension
    public static class DescriptorImpl extends Descriptor<MetadataDisplayOptions> {

        @Override
        public String getDisplayName() {
            return "not used";
        }
        
    }
}
