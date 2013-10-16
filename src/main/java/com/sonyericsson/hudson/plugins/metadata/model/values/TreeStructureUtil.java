/*
 *  The MIT License
 *
 *  Copyright 2011 Sony Ericsson Mobile Communications. All rights reserved.
 *  Copyright 2012 Sony Mobile Communications AB. All rights reserved.
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
package com.sonyericsson.hudson.plugins.metadata.model.values;

import com.sonyericsson.hudson.plugins.metadata.model.Metadata;
import com.sonyericsson.hudson.plugins.metadata.model.MetadataContainer;
import com.sonyericsson.hudson.plugins.metadata.model.MetadataParent;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * Utility methods for easier creation of tree structures of values.
 *
 * @author Robert Sandell &lt;robert.sandell@sonyericsson.com&gt;
 */
public abstract class TreeStructureUtil {

    /**
     * Private utility constructor.
     */
    private TreeStructureUtil() {

    }

    /**
     * Adds a {@link StringMetadataValue} to the root node with the specified path.
     *
     * @param root        the root to add the tree to.
     * @param value       the string value of the leaf node.
     * @param description the description of the leaf node.
     * @param path        the path to the leaf from the root.
     * @return true if there was no merge conflicts.
     */
    public static boolean addValue(MetadataParent root, String value, String description, String... path) {
        return addValue(root, value, description, true, false, path);
    }

    /**
     * Adds a {@link StringMetadataValue} to the root node with the specified path.
     *
     * @param root        the root to add the tree to.
     * @param value       the string value of the leaf node.
     * @param description the description of the leaf node.
     * @param generated what the value's
     *                   {@link com.sonyericsson.hudson.plugins.metadata.model.values.MetadataValue#isGenerated()}
     *                   should be.
     * @param exposedToEnvironment if this value should be exposed to the build as an
     * environment variable.
     * @param path        the path to the leaf from the root.
     * @return true if there was no merge conflicts.
     */
    public static boolean addValue(MetadataParent root, String value, String description,
                                   boolean generated, boolean exposedToEnvironment, String... path) {
        //TODO: fix null
        StringMetadataValue sVal = new StringMetadataValue(path[path.length - 1], description, value,
                exposedToEnvironment, null);
        sVal.setGenerated(generated);
        return addValue(root, sVal, generated, Arrays.copyOf(path, path.length - 1));
    }

    /**
     * Adds a {@link DateMetadataValue} to the root node with the specified path.
     *
     * @param root        the root to add the tree to.
     * @param value       the date value of the leaf node.
     * @param description the description of the leaf node.
     * @param exposedToEnvironment if this value should be exposed to the build as an
     *                      environment variable.
     *  @param path        the path to the leaf from the root.
     * @return true if there was no merge conflicts.
     */
    public static boolean addValue(MetadataParent root, Date value, String description,
                                   boolean exposedToEnvironment, String... path) {
        DateMetadataValue sVal = new DateMetadataValue(path[path.length - 1], description, value, exposedToEnvironment);
        sVal.setGenerated(true);
        return addValue(root, sVal, Arrays.copyOf(path, path.length - 1));
    }

    /**
     * Adds a value with the specified path to the root.
     *
     * @param root       the root to add the tree to.
     * @param value      the value of the leaf.
     * @param parentPath the path of the parent of the leaf from the root.
     * @return true if there was no merge conflicts.
     */
    public static boolean addValue(MetadataParent root, AbstractMetadataValue value, String... parentPath) {
        return addValue(root, value, true, parentPath);
    }

    /**
     * Adds a value with the specified path to the root.
     *
     * @param root       the root to add the tree to.
     * @param value      the value of the leaf.
     * @param generated  the parentPath should be marked as generated or not.
     * @param parentPath the path of the parent of the leaf from the root.
     * @return true if there was no merge conflicts.
     */
    public static boolean addValue(MetadataParent root, AbstractMetadataValue value,
                                   boolean generated, String... parentPath) {
        if (parentPath == null || parentPath.length <= 0) {
            return root.addChild(value) == null;
        } else {
            TreeNodeMetadataValue path = createPath(value, generated, parentPath);
            return root.addChild(path) == null;
        }
    }

    /**
     * Creates a path where the last element is a string with the provided value and description.
     *
     * @param value       the value
     * @param description the description
     * @param path        the full path to the leaf.
     * @return the tree.
     */
    public static TreeNodeMetadataValue createPath(String value, String description, String... path) {
        return createPath(value, description, true, false, path);
    }

    /**
     * Creates a path where the last element is a string with the provided value and description.
     *
     * @param value       the value
     * @param description the description
     * @param generated what the value's
     *                   {@link com.sonyericsson.hudson.plugins.metadata.model.values.MetadataValue#isGenerated()}
     *                   should be.
     * @param exposedToEnvironment if this value should be exposed to the build as an
     *                   environment variable.
     * @param path        the full path to the leaf.
     * @return the tree.
     */
    public static TreeNodeMetadataValue createPath(String value, String description, boolean generated,
                                                   boolean exposedToEnvironment, String... path) {
        StringMetadataValue str = new StringMetadataValue(path[path.length - 1], description, value,
                exposedToEnvironment, null);
        str.setGenerated(generated);
        return createPath(str, generated, Arrays.copyOf(path, path.length - 1));
    }

    /**
     * Creates a path where the last element is a string with the provided value and description.
     *
     * @param value       the value
     * @param description the description
     * @param exposedToEnvironment if this value should be exposed to the build as an
     *                   environment variable.
     * @param path        the full path to the leaf.
     * @return the tree.
     */
    public static TreeNodeMetadataValue createPath(Date value, String description,
                                                   boolean exposedToEnvironment, String... path) {
        DateMetadataValue val = new DateMetadataValue(path[path.length - 1], description, value, exposedToEnvironment);
        val.setGenerated(true);
        return createPath(val, Arrays.copyOf(path, path.length - 1));
    }

    /**
     * Creates a tree structured path with the provided leaf at the end. The value's {@link
     * com.sonyericsson.hudson.plugins.metadata.model.values.MetadataValue#isGenerated()} will be true.
     *
     * @param leaf       the leaf to put in the end.
     * @param parentPath the path to the leaf.
     * @return the root node of the path.
     */
    public static TreeNodeMetadataValue createPath(AbstractMetadataValue leaf, String... parentPath) {
        return createPath(leaf, true, parentPath);
    }

    /**
     * Creates a tree structured path with the provided leaf at the end.
     *
     * @param leaf       the leaf to put in the end.
     * @param generated  what the value's
     *                   {@link com.sonyericsson.hudson.plugins.metadata.model.values.MetadataValue#isGenerated()}
     *                   should be.
     * @param parentPath the path to the leaf.
     * @return the root node of the path.
     */
    public static TreeNodeMetadataValue createPath(AbstractMetadataValue leaf, boolean generated, String... parentPath) {
        if (parentPath == null || parentPath.length < 1) {
            throw new IllegalArgumentException("The leaf must have at least one parent.");
        }
        TreeNodeMetadataValue root = null;
        TreeNodeMetadataValue parent = null;

        for (String name : parentPath) {
            TreeNodeMetadataValue val = new TreeNodeMetadataValue(name);
            val.setGenerated(generated);
            if (parent != null) {
                parent.addChild(val);
            }
            parent = val;
            if (root == null) {
                root = val;
            }
        }
        parent.addChild(leaf);
        return root;
    }

    /**
     * Creates a straight tree-path. The method returns an array where index 0 is the root and index 1 is the leaf. The
     * value's {@link com.sonyericsson.hudson.plugins.metadata.model.values.MetadataValue#isGenerated()} will be true.
     *
     * @param description the description of the root.
     * @param path        the path to create.
     * @return the root and the leaf.
     */
    public static TreeNodeMetadataValue[] createTreePath(String description, String... path) {
        return createTreePath(description, true, path);
    }

    /**
     * Creates a straight tree-path. The method returns an array where index 0 is the root and index 1 is the leaf.
     *
     * @param description the description of the root.
     * @param generated   what the value's
     *                    {@link com.sonyericsson.hudson.plugins.metadata.model.values.MetadataValue#isGenerated()}
     *                    should be.
     * @param path        the path to create.
     * @return the root and the leaf.
     */
    public static TreeNodeMetadataValue[] createTreePath(String description, boolean generated, String... path) {
        TreeNodeMetadataValue[] arr = new TreeNodeMetadataValue[2];
        arr[1] = new TreeNodeMetadataValue(path[path.length - 1], description);
        arr[1].setGenerated(generated);
        arr[0] = createPath(arr[1], generated, Arrays.copyOf(path, path.length - 1));
        return arr;
    }

    /**
     * Returns the node with the given path.
     *
     * @param root the root to start from when searching for the node with the given path.
     * @param path the path for which a node should be returned.
     * @param <T>  The type of metadata.
     * @return the value or null if it wasn't found.
     */
    public static <T extends Metadata> T getPath(MetadataParent<T> root, String... path) {
        if (path == null) {
            return null;
        }
        MetadataParent<T> parent = root;
        T currentValue = null;
        for (int i = 0; i < path.length; i++) {
            String name = path[i];
            currentValue = parent.getChild(name);
            if (currentValue == null) {
                return null;
            } else if (i == path.length - 1) {
                return currentValue;
            } else if (currentValue instanceof MetadataParent) {
                parent = (MetadataParent)currentValue;
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * Returns the node with the given path.
     *
     * @param collection a collection of nodes to use as root nodes when searching for the node with the given path.
     * @param path the path for which a node should be returned.
     * @param <T>  The type of metadata.
     * @return the value or null if it wasn't found.
     */
    public static <T extends Metadata> T getPath(Collection<T> collection, String... path) {
        if (path == null || path.length == 0) {
            return null;
        }
        String name = path[0];
        for (T metadata : collection) {
            if (metadata.getName().equalsIgnoreCase(name)) {
                if (path.length == 1) {
                    return metadata;
                }
                if (metadata instanceof MetadataParent) {
                    String[] subPath = Arrays.copyOfRange(path, 1, path.length);
                    return getPath((MetadataParent<T>)metadata, subPath);
                }
            }
        }
        return null;
    }

    /**
     * Returns the leaf with the given path.
     *
     * @param root the root to start from when searching for the leaf node with the given path.
     * @param path the path for which a leaf node should be returned.
     * @param <T>  The type of metadata.
     * @return the leaf or null if it wasn't found or if the path doesn't represent a leaf.
     */
    public static <T extends Metadata> T getLeaf(MetadataParent<T> root, String... path) {
        T value = getPath(root, path);
        if (value == null) {
            return null;
        } else if (value instanceof MetadataParent) {
            return null;
        } else {
            return value;
        }
    }

    /**
     * Returns the leaf with the given path.
     *
     * @param collection the collection to start from.
     * @param path the path for which a leaf node should be returned.
     * @param <T>  The type of metadata.
     * @return the leaf or null if it wasn't found or if the path doesn't represent a leaf.
     */
    public static <T extends Metadata> T getLeaf(Collection<T> collection, String... path) {
        T value = getPath(collection, path);
        if (value == null || value instanceof MetadataParent) {
            return null;
        }
        return value;
    }

    /**
     * Adds all the leaves in the collection to the newCollection.
     *
     * @param collection the Collection to find the leaves in.
     * @param newCollection the result.
     * @param <T> the type of Metadata.
     */
    public static <T extends Metadata> void findLeaves(Collection<T> collection, Collection<T> newCollection) {
        if (collection == null || collection.isEmpty()) {
            return;
        }
        for (Metadata def : collection) {
            if (def instanceof MetadataParent) {
                Collection children = ((MetadataParent)def).getChildren();
                findLeaves(children, newCollection);
            } else {
                newCollection.add((T)def);
            }
        }
    }


    /**
     * Prints the value and it's child if any into a structured string.
     *
     * @param value the value to print
     * @param tabs  the current level
     * @return a pretty string.
     */
    public static String prettyPrint(MetadataValue value, String tabs) {
        StringBuffer str = new StringBuffer(tabs);
        str.append(value.getName()).append("\n");
        if (value instanceof MetadataParent) {
            MetadataParent node = (MetadataParent)value;
            prettyPrint(node.getChildren(), tabs + "\t");
        }
        return str.toString();
    }

    /**
     * Prints the values and their children if any into a structured string.
     *
     * @param values the values to print
     * @param tabs   the current level.
     * @return a pretty string.
     */
    public static String prettyPrint(Collection<MetadataValue> values, String tabs) {
        StringBuffer str = new StringBuffer();
        for (MetadataValue subValue : values) {
            str.append(prettyPrint(subValue, tabs));
        }
        return str.toString();
    }

    /**
     * Find the first MetadataContainer that is an ancestor to the given Metadata.
     *
     * @param metadata the Metadata to find a MetadataContainer for.
     * @return the first MetadataContainer ancestor.
     */
    public static MetadataContainer getContainer(Metadata metadata) {
        MetadataParent currentParent = metadata.getParent();
        while (!(currentParent instanceof MetadataContainer)) {
            if (currentParent instanceof Metadata) {
                currentParent = ((Metadata)currentParent).getParent();
            } else {
                return null;
            }
        }
        return (MetadataContainer)currentParent;
    }
}
