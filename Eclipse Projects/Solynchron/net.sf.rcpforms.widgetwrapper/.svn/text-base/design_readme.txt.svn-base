Design Decisions in RCPForms
============================

1. Widget Wrappers

SWT widgets are wrapped in favour of subclassing to support additional functionality like
required field decoration, disabled-rendering inheritance (e.g. required fields in a disabled composite
are not rendered as required).

Wrapper inheritance was favoured against generic wrapper for widget.

2. Model Abstraction

For Table and Master Detail support property specification should not be per instance but
as meta-information which is independent of the instance of the field.

Getting the correct ModelAdapter is based on meta-class not instance, since instance is not
always known when defining the binding, e.g. table column specification.

Meta class Examples:
- for a java bean: Class (bean.getClass())
- for an EMF EObject:  EClass (eobject.getEClass())

Currently there are 2 implementations of model adapters together with their unit tests available,
one for a JavaBean and another for an EMF object.

ATTENTION: the EMF implementation requires Eclipse 3.4 (Ganymede)!