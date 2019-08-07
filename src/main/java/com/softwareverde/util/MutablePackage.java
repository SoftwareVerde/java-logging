package com.softwareverde.util;

import java.util.ArrayList;
import java.util.List;

public class MutablePackage implements Package {
    protected static Boolean areEqual(final String string0, final String string1) {
        if ( (string0 == null) != (string1 == null) ) { return false; }
        if (string0 == null) { return true; }
        return string0.equals(string1);
    }

    public static MutablePackage newRootPackage() {
        return new MutablePackage(null, null);
    }

    public static Boolean areNamesEqual(final Package pkg0, final Package pkg1) {
        return MutablePackage.areEqual(pkg0.getName(), pkg1.getName());
    }

    protected MutablePackage _parent;
    protected final String _name;
    protected final List<MutablePackage> _children = new ArrayList<MutablePackage>();

    protected MutablePackage _getRoot() {
        MutablePackage root = this;

        while (root._parent != null) {
            root = root._parent;
        }

        return root;
    }

    protected MutablePackage _getChild(final String fullPackageName) {
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

        for (final MutablePackage childPackage : _children) {
            if (MutablePackage.areEqual(name, childPackage._name)) {
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

    protected void _attachTo(final MutablePackage anchorPackage) {
        if (anchorPackage.hasChild(_name)) {
            final MutablePackage anchorChildPackage = anchorPackage._getChild(_name);
            for (final MutablePackage childPackage : _children) {
                childPackage._attachTo(anchorChildPackage);
            }
        }
        else {
            _parent = anchorPackage;
            anchorPackage._children.add(this);
        }
    }

    protected MutablePackage() {
        _parent = null;
        _name = null;
    }

    protected MutablePackage(final MutablePackage parent, final String name) {
        _parent = parent;
        _name = name;
    }

    public void mergeInPackage(final MutablePackage pkg) {
        final MutablePackage thisRoot = _getRoot();
        final MutablePackage pkgRoot = pkg._getRoot();

        if (! MutablePackage.areEqual(thisRoot._name, pkgRoot._name)) {
            System.err.println("NOTE: Attempted to add unrelated packages.");
            return;
        }

        for (final MutablePackage pkgChild : pkgRoot._children) {
            pkgChild._attachTo(thisRoot);
        }
    }

    public void clear() {
        _children.clear();
    }

    @Override
    public Package getRoot() {
        return _getRoot();
    }

    @Override
    public Boolean hasParent() {
        return (_parent != null);
    }

    @Override
    public Package getParent() {
        return _parent;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public List<Package> getChildren() {
        return new ArrayList<Package>(_children);
    }

    @Override
    public List<Package> getLeafChildren() {
        final ArrayList<Package> leafChildren = new ArrayList<Package>();

        for (final Package childPackage : _children) {
            final List<Package> subChildren = childPackage.getLeafChildren();
            if (subChildren.isEmpty()) {
                leafChildren.add(childPackage);
            }
            else {
                leafChildren.addAll(subChildren);
            }
        }

        return leafChildren;
    }

    @Override
    public int getDepth() {
        int depth = 0;

        Package parent = _parent;
        while (parent != null) {
            parent = parent.getParent();
            depth += 1;
        }

        return depth;
    }

    @Override
    public Package getChild(final String name) {
        return _getChild(name);
    }

    @Override
    public Boolean hasChild(final String name) {
        return (_getChild(name) != null);
    }

    @Override
    public Boolean isRoot() {
        return ( (_name == null) && (_parent == null) );
    }

    @Override
    public String toString() {
        if ( (_parent == null) && (_name == null) ) { return ""; }

        if ( (_parent == null) || _parent.isRoot() ) {
            return (_name != null ? _name : "");
        }

        return (_parent.toString() + "." + _name);
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) { return false; }
        if (! (object instanceof Package)) { return false; }
        if (this == object) { return true; }

        final Package packageObject = (Package) object;
        if ( (_parent == null) != (packageObject.getParent() == null) ) { return false; }
        if (! MutablePackage.areEqual(_name, packageObject.getName())) { return false; }

        if (_parent != null) {
            if (! _parent.equals(packageObject.getParent())) { return false; }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return ((_parent != null ? _parent.hashCode() : 0) + (_name != null ? _name.hashCode() : 0));
    }
}