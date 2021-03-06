package com.github.goldin.spock.extensions.with

import org.gcontracts.annotations.Ensures
import org.gcontracts.annotations.Requires
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.FeatureInfo
import org.spockframework.runtime.model.SpecInfo

/**
 * {@code @With} extension.
 */
class WithExtension extends AbstractAnnotationDrivenExtension<With>
{
    private final List<Object>              specObjects    = []  // Spec-scoped @With objects
    private final Map<String, List<Object>> featureObjects = [:] // Mapping of feature name to its @With objects


    /**
     * Reads {@code @With} annotation as a closure and converts its result to list of objects.
     *
     * @param annotation annotation to read
     * @return objects retrieved
     */
    @Requires({ annotation })
    @Ensures({ result })
    private Collection<?> objects( With annotation )
    {
       /**
        * ( '1', '1' ) are dummy values, see http://goo.gl/VnJZP
        */
        Closure closure = ( Closure ) annotation.value().newInstance( '1', '1' )
        Object  objects = closure()
        ( objects instanceof List ? objects : [ objects ] )
    }


    @Override
    void visitSpecAnnotation ( With annotation, SpecInfo spec )
    {
        specObjects.addAll( objects( annotation ))
    }


    @Override
    @Requires({ annotation && feature })
    void visitFeatureAnnotation ( With annotation, FeatureInfo feature )
    {
        featureObjects[ feature.name ] = objects( annotation )
    }


    @Override
    @Requires({ spec })
    void visitSpec ( SpecInfo spec )
    {
        for ( featureInfo in spec.features )
        {
            final delegateObjects = featureObjects[ featureInfo.name ]

            if ( delegateObjects )
            {
                featureInfo.addInterceptor( new WithInterceptor(( List ) delegateObjects + specObjects ))
            }
            else if ( specObjects )
            {
                featureInfo.addInterceptor( new WithInterceptor( specObjects ))
            }
        }
    }
}
