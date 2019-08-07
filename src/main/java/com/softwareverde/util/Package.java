package com.softwareverde.util;

import com.softwareverde.logging.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Package {
    public static Boolean areEqual(final String string0, final String string1) {
        if ( (string0 == null) != (string1 == null) ) { return false; }
        if (string0 == null) { return true; }
        return string0.equals(string1);
    }

    public static Boolean areNamesEqual(final Package pkg0, final Package pkg1) {
        return Package.areEqual(pkg0.name, pkg1.name);
    }

    protected static final Package ROOT_PACKAGE = new Package(null, null);

    public static Package getRootPackage() {
        return ROOT_PACKAGE;
    }

    public static Package fromString(final String packageName) {
        Package rootPackage = new Package(null, null);
        for (final String segmentName : packageName.split("\\.")) {
            final Package newPackage = new Package(rootPackage, segmentName);
            rootPackage.children.add(newPackage);
            rootPackage = newPackage;
        }
        return rootPackage;
    }

    public Package parent;
    public final String name;
    public final List<Package> children = new ArrayList<Package>();
    public Logger.Level level;

    protected Package _getRoot() {
        Package root = this;

        while (root.parent != null) {
            root = root.parent;
        }

        return root;
    }

    public Package _getChild(final String fullPackageName) {
        if (fullPackageName == null) { return null; }

        final String name;
        final String remainingPackages;
        {
            if (! fullPackageName.contains(".")) {
                name = fullPackageName;
                remainingPackages = "";
            }
            else {
                final int index = fullPackageName.indexOf(".");
                name = fullPackageName.substring(0, index);
                remainingPackages = (((index + 1) < fullPackageName.length()) ? fullPackageName.substring(index + 1) : "");
            }
        }

        for (final Package childPackage : this.children) {
            if (Package.areEqual(name, childPackage.name)) {
                if (remainingPackages.isEmpty()) {
                    return childPackage;
                }
                else {
                    return childPackage._getChild(remainingPackages);
                }
            }
        }

        return null;
    }

    public Package(final String name) {
        this.parent = null;
        this.name = name;
    }

    public Package(final Package parent, final String name) {
        this.parent = parent;
        this.name = name;
    }

    public Package getRoot() {
        return _getRoot();
    }

    public int getDepth() {
        int depth = 0;

        Package parent = this.parent;
        while (parent != null) {
            parent = parent.parent;
            depth += 1;
        }

        return depth;
    }

    public List<String> collectNamesAscending() {
        final List<String> names = new ArrayList<String>();
        names.add(this.name);

        Package parent = this.parent;
        while (parent != null) {
            names.add(parent.name);
            parent = parent.parent;
        }

        Collections.reverse(names);

        return names;
    }

    public Package getChild(final String name) {
        return _getChild(name);
    }

    public Boolean hasChild(final String name) {
        return (_getChild(name) != null);
    }

    protected void attachTo(final Package anchorPackage) {
        if (anchorPackage.hasChild(this.name)) {
            final Package anchorChildPackage = anchorPackage.getChild(this.name);
            for (final Package childPackage : this.children) {
                childPackage.attachTo(anchorChildPackage);
            }
        }
        else {
            this.parent = anchorPackage;
            anchorPackage.children.add(this);
        }
    }

    public void consumePackage(final Package pkg) {
        final Package thisRoot = _getRoot();
        final Package pkgRoot = pkg.getRoot();

        if (! Package.areEqual(thisRoot.name, pkgRoot.name)) {
            System.err.println("NOTE: Attempted to add unrelated packages.");
            return;
        }

        for (final Package pkgChild : pkgRoot.children) {
            final String pkgChildName = pkgChild.name;
            pkgChild.attachTo(thisRoot);
        }
    }

    public Boolean isRoot() {
        return ( (this.name == null) && (this.parent == null) );
    }

    @Override
    public String toString() {
        if (this.isRoot()) { return ""; }
        if ( (this.parent == null) || this.parent.isRoot() ) {
            return (this.name != null ? this.name : "");
        }

        return (this.parent.toString() + "." + this.name);
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) { return false; }
        if (! (object instanceof Package)) { return false; }
        if (this == object) { return true; }

        final Package packageObject = (Package) object;
        if ( (this.parent == null) != (packageObject.parent == null) ) { return false; }
        if (! Package.areEqual(this.name, packageObject.name)) { return false; }

        if (this.parent != null) {
            if (! this.parent.equals(packageObject.parent)) { return false; }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return ((parent != null ? parent.hashCode() : 0) + (name != null ? name.hashCode() : 0));
    }
}