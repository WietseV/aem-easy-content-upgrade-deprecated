/*
 *  Copyright 2018 Valtech GmbH
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>
 */
package de.valtech.aecu.core.groovy.console.bindings.traversers;

import de.valtech.aecu.core.groovy.console.bindings.actions.Action;
import de.valtech.aecu.core.groovy.console.bindings.filters.FilterBy;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import javax.annotation.Nonnull;
import java.util.Iterator;

/**
 * @author Roxana Muresan
 */
public class ForChildResourcesOf implements TraversData {

    private String path;

    public ForChildResourcesOf(@Nonnull String path) {
        this.path = path;
    }


    @Override
    public void traverse(@Nonnull ResourceResolver resourceResolver, FilterBy filter, @Nonnull Action action, @Nonnull StringBuffer stringBuffer, boolean dryRun) throws PersistenceException {
        Resource parentResource = resourceResolver.getResource(path);
        if (parentResource != null) {
            Iterator<Resource> resourceIterator = resourceResolver.listChildren(parentResource);
            while (resourceIterator.hasNext()) {
                Resource resource = resourceIterator.next();
                if (filter == null || filter.filter(resource)) {
                    stringBuffer.append(action.doAction(resource) + "\n");
                }
            }
            if (!dryRun) {
                resourceResolver.commit();
            }
        }
    }
}
