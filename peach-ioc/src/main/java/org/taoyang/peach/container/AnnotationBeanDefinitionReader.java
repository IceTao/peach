/*
 * Author:  taoyang <ice@taoyang.org>
 * Created: 2017-05-08
 */
package org.taoyang.peach.container;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;
import org.taoyang.peach.container.annotation.Component;


public class AnnotationBeanDefinitionReader implements BeanDefinitionReader {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationBeanDefinitionReader.class);

    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    private Registry registry;

    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private MetadataReaderFactory metadataReaderFactory =
            new CachingMetadataReaderFactory(this.resourcePatternResolver);

    private String resourcePattern = DEFAULT_RESOURCE_PATTERN;

    private final List<TypeFilter> includeFilters = new LinkedList<>();

    public AnnotationBeanDefinitionReader(Registry registry) {
        registerDefaultFilters();
        this.registry = registry;
    }

    @Override
    public int load(String pkgName) {
        return load(new String[] {pkgName});
    }

    @Override
    public int load(String... pkgNames) {
        int count = 0;
        Set<BeanDefinition> beanDefinitions = new LinkedHashSet<>();
        for (String pkgName : pkgNames) {
            Set<BeanDefinition> candidates = findComponents(pkgName);
            for (BeanDefinition definition : candidates) {
                // TODO set scope AND alias
                registry.registerBeanDefinition(generateName(definition), definition);
                count += 1;
            }
        }

        return count;
    }

    private void scan(String pkgName) {
        Set<BeanDefinition> beanDefinitions;
    }

    @Override
    public ClassLoader classLoader() {
        return null;
    }

    @Override
    public Registry registry() {
        return null;
    }

    public Set<BeanDefinition> findComponents(String pkgNames) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                ClassUtils.convertClassNameToResourcePath(pkgNames) + "/" + this.resourcePattern;
        logger.debug("Scanning " + packageSearchPath);
        try {
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                logger.debug("Scanning " + resource);
                if (resource.isReadable()) {
                    MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                    if (isComponent(metadataReader)) {
                        DefaultBeanDefinition definition = new DefaultBeanDefinition(metadataReader);
                        definition.setResource(resource);

                        if (isCandidateComponent(definition)) {
                            logger.debug("Identified candidate component class: " + resource);
                            candidates.add(definition);
                        } else {
                            logger.debug("Ignored because not a concrete top-level class: " + resource);
                        }
                    } else {
                        logger.trace("Ignored because not matching any filter: " + resource);
                    }
                } else {
                    logger.trace("Ignored because not readable: " + resource);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return candidates;
    }

    private boolean isComponent(MetadataReader metadataReader) throws IOException {
        for(TypeFilter tf : this.includeFilters) {
            if (tf.match(metadataReader, this.metadataReaderFactory)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return (beanDefinition.getMetadata().isConcrete() && beanDefinition.getMetadata().isIndependent());
    }

    private void registerDefaultFilters() {
        this.includeFilters.add(new AnnotationTypeFilter(Component.class));
    }

    private String generateName(BeanDefinition definition) {
        return definition.getBeanClassName();
    }


}
