package com.btl.doc.it;

import com.google.common.collect.Lists;
import org.springframework.hateoas.Link;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.snippet.Snippet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;

public class DocumentationFields {

    /**
     * About Field Description
     *
     * @return see desc
     */
    public static List<FieldDescriptor> getAboutFieldDescription() {
        return Lists.newArrayList(
                fieldWithPath("content.gitCommitDate").description("Information about git commit date"),
                fieldWithPath("content.gitCommitSha1").description("Information about git commit Sha1"),
                fieldWithPath("content.gitCommitBranch").description("Information about git commit branch"),
                fieldWithPath("content.version").description("Information about application version"),
                fieldWithPath("content.gitBuildDate").description("Information about git build date")
        );
    }

    /**
     * Audit Trail Field Description
     *
     * @return see desc
     */
    public static List<FieldDescriptor> getAuditTrailFieldDescription() {
        return Lists.newArrayList(
                fieldWithPath("id").description("Audit trail id"),
                fieldWithPath("userId").description("Audit trail userId"),
                fieldWithPath("category").description("Audit trail category"),
                fieldWithPath("data").description("Audit trail data")
        );
    }

    /**
     * Add Field Descriptors for page use and modify descriptors path to add content
     *
     * @param descriptors the field descriptors
     * @return see desc
     */
    public static List<FieldDescriptor> addPageFields(final List<FieldDescriptor> descriptors) {
        ArrayList<FieldDescriptor> newDescriptors = Lists.newArrayList();
        newDescriptors.addAll(descriptors.stream().map(descriptor -> fieldWithPath("content[]." + descriptor.getPath())
                .description(descriptor.getDescription())).collect(Collectors.toList()));
        newDescriptors.addAll(
                Lists.newArrayList(
                        fieldWithPath("content").description("Table of results"),
                        fieldWithPath("totalPages").description("Number of total pages"),
                        fieldWithPath("totalElements").description("Total amount of elements"),
                        fieldWithPath("last").description("Boolean last page"),
                        fieldWithPath("numberOfElements").description("Number of elements for current result set"),
                        fieldWithPath("first").description("Boolean first page"),
                        fieldWithPath("sort").description("Sort parameter for pagination"),
                        fieldWithPath("size").description("Size of result set"),
                        fieldWithPath("number").description("Number for current page")
                )
        );
        return newDescriptors;
    }

    /**
     * Fields are now in a table.
     * Add a []. for all paths
     *
     * @param descriptors the field descriptors
     * @return see desc
     */
    public static List<FieldDescriptor> addFieldsInTable(final List<FieldDescriptor> descriptors) {
        return addFieldsInResources(descriptors, "[]");
    }

    /**
     * Fields are now in a Resources object
     * Add given string for all paths
     *
     * @param descriptors the field descriptors
     * @return see desc
     */
    public static List<FieldDescriptor> addFieldsInResources(final List<FieldDescriptor> descriptors, final String resourcesList) {
        ArrayList<FieldDescriptor> newDescriptors = Lists.newArrayList();
        for (final FieldDescriptor descriptor : descriptors) {
            FieldDescriptor newField = fieldWithPath(resourcesList + "." + descriptor.getPath())
                    .description(descriptor.getDescription()).type(descriptor.getType());
            newDescriptors.add(descriptor.isOptional() ? newField.optional() : newField);
        }
        return newDescriptors;
    }

    /**
     * Fields are now in a Paged Resources object
     * Add given string for all paths
     *
     * @param descriptors the field descriptors
     * @return see desc
     */
    public static List<FieldDescriptor> addFieldsInPagedResources(final List<FieldDescriptor> descriptors, final String resourcesList) {
        List<FieldDescriptor> newDescriptors = addFieldsInResources(descriptors, resourcesList);
        newDescriptors.add(fieldWithPath("page").description("Page details"));
        newDescriptors.add(fieldWithPath("page.size").description("Size of result set"));
        newDescriptors.add(fieldWithPath("page.totalElements").description("Total amount of elements"));
        newDescriptors.add(fieldWithPath("page.totalPages").description("Number of total pages"));
        newDescriptors.add(fieldWithPath("page.number").description("Number for current page"));
        newDescriptors.add(fieldWithPath("_links").description("<<overview-hypermedia>> to other resources"));
        return newDescriptors;
    }

    /**
     * User Field Description
     *
     * @return see desc
     */
    public static List<FieldDescriptor> getUserFieldDescription() {
        return Lists.newArrayList(
                fieldWithPath("id").description("User id"),
                fieldWithPath("spoofedBy").description("Spoofer id if necessary").optional().type(String.class),
                fieldWithPath("password").description("User password, not present when requested").optional().type(String.class),
                fieldWithPath("firstname").description("User firstname"),
                fieldWithPath("lastname").description("User lastname"),
                fieldWithPath("email").description("User email"),
                fieldWithPath("authorities").description("User authorities").optional()
                        .attributes(
                                key("authority").value("Authority type"),
                                key("scope").value("Authority scope")
                        ).type(List.class),
                fieldWithPath("_links").description("<<overview-hypermedia>> to other resources").optional().type(Object.class),
                fieldWithPath("_embedded").description("List of user farms").optional().type(List.class)
                        .attributes(
                                key("farmList").value("List of user farms")
                        ).type(List.class)
        );
    }

    /**
     * Layer Field Description
     *
     * @return see desc
     */
    public static List<FieldDescriptor> getLayerFieldDescription() {
        return Lists.newArrayList(
                fieldWithPath("id").description("Layer id"),
                fieldWithPath("name").description("Name of the extracted attribute"),
                fieldWithPath("layerSourceId").description("Id of the parent layer source"),
                fieldWithPath("data").description("GeoJson representation of the layer").type(String.class).optional(),
                fieldWithPath("caption").description("Layer caption"),
                fieldWithPath("_links").description("Link of the layer")
        );
    }

    /**
     * LayerSource Field Description
     *
     * @return see desc
     */
    public static List<FieldDescriptor> getLayerSourceFieldDescription() {
        return Lists.newArrayList(
                fieldWithPath("id").description("Layer Source id"),
                fieldWithPath("layerTypeId").description("Layer source type id"),
                fieldWithPath("relationId").description("Layer source relation id"),
//                fieldWithPath("relationType").description("Layer source relation type. Can be : " + Joiner.on(", ").join(LayerRelationType.values())),
                fieldWithPath("name").description("Layer source name"),
                fieldWithPath("_links").description("Link of the layer source")
        );
    }

    /**
     * Farm Circle Field Description
     *
     * @return see desc
     */
    public static List<FieldDescriptor> getFarmCircleFieldDescription() {
        return Lists.newArrayList(
                fieldWithPath("_embedded.farmCircleList[].id").description("Farm circle id"),
                fieldWithPath("_embedded.farmCircleList[].name").description("Farm circle name"),
                fieldWithPath("_embedded.farmCircleList[].agronomistId").description("Creator Id"),
                fieldWithPath("_embedded.farmCircleList[].farms").description("Farm circle <<farm>> list"),
                fieldWithPath("_embedded.farmCircleList[]._links").description("<<overview-hypermedia>> to other resources")
        );
    }

    /**
     * Farm Field Description
     *
     * @return see desc
     */
    public static List<FieldDescriptor> getFarmFieldDescription(final boolean wLinks) {
        List<FieldDescriptor> fieldDescriptors = Lists.newArrayList(
                fieldWithPath("id").description("Farm id"),
                fieldWithPath("name").description("Farm name"),
                fieldWithPath("address").description("Farm Address")
        );
        if (wLinks) {
            fieldDescriptors.add(fieldWithPath("_links").description("<<overview-hypermedia>> to other resources"));
        }
        return fieldDescriptors;
    }

    /**
     * Synchronization Field Description
     *
     * @return see desc
     */
    public static List<FieldDescriptor> getSynchronizationFieldDescription() {
        return Lists.newArrayList(
                fieldWithPath("farm").description("Farm information"),
                fieldWithPath("fields").description("Fields information"),
                fieldWithPath("crops").description("Crops information"),
                fieldWithPath("layerSources").description("Layer sources information"),
                fieldWithPath("layerTypes").description("Layer types information"),
                fieldWithPath("layerCategories").description("Layer categories information"),
                fieldWithPath("observations").description("Farm observations"),
                fieldWithPath("observationTypes").description("Observation types"),
                fieldWithPath("interventions").description("Farm interventions"),
                fieldWithPath("interventionTypes").description("Intervention types")
        );
    }

    /**
     * Field entity Field Description
     *
     * @return see desc
     */
    public static List<FieldDescriptor> getFieldFieldDescription() {
        return Lists.newArrayList(
                fieldWithPath("id").description("Field id"),
                fieldWithPath("name").description("Field name"),
                fieldWithPath("description").description("Field description"),
                fieldWithPath("farmId").description("<<farm>> Id for current field"),
                fieldWithPath("_embedded").description("Holds the bounds layer of the field").optional().type(List.class),
                fieldWithPath("_links").description("<<overview-hypermedia>> to other resources").optional().type(Link.class)

        );
    }

    /**
     * Crop entity Field Description
     *
     * @return see desc
     */
    public static List<FieldDescriptor> getCropFieldDescription() {
        return Lists.newArrayList(
                fieldWithPath("id").description("Crop id"),
                fieldWithPath("fieldId").description("Crop field id"),
                fieldWithPath("startDate").description("Crop start date"),
                fieldWithPath("endDate").description("Crop end date").optional().type(Date.class),
                fieldWithPath("speciesId").description("Crop species id").optional().type(Long.class),
                fieldWithPath("varietyId").description("Crop variety id").optional().type(Long.class),
                fieldWithPath("_links").description("<<overview-hypermedia>> to other resources").optional().type(Link.class)
        );
    }

    /**
     * Tile entity Field Description
     *
     * @return see desc
     */
    public static List<FieldDescriptor> getTileFieldDescription() {
        return Lists.newArrayList(
                fieldWithPath("tiles[].x").description("Tile x"),
                fieldWithPath("tiles[].y").description("Tile y"),
                fieldWithPath("tiles[].zoom").description("Tile zoom"),
                fieldWithPath("tilesUrlPattern").description("Tile url pattern")
        );
    }

    /**
     * Observation entity Field Description
     *
     * @return see desc
     */
    public static List<FieldDescriptor> getObservationFieldDescription(final boolean wLinks) {
        List<FieldDescriptor> fieldDescriptors = Lists.newArrayList(
                fieldWithPath("id").description("Observation id"),
                fieldWithPath("cropId").description("Observation crop id"),
                fieldWithPath("userId").description("Observation creator id"),
                fieldWithPath("observationType").description("Observation type"),
                fieldWithPath("observationDate").description("Observation date"),
                fieldWithPath("creationDate").description("Observation creation date"),
                fieldWithPath("attributes").description("Observation name table")
        );

        if (wLinks) {
            fieldDescriptors.add(fieldWithPath("_links").description("<<overview-hypermedia>> to other resources"));
        }

        return fieldDescriptors;
    }

    /**
     * Observation/Intervention type Field Description
     *
     * @return see desc
     */
    public static List<FieldDescriptor> getObservationAndInterventionTypeFieldDescription() {
        return Lists.newArrayList(
                fieldWithPath("name").description("Type name"),
                fieldWithPath("code").description("Type translation code"),
                fieldWithPath("attributes").description("Type attributes"),
                fieldWithPath("_links").description("<<overview-hypermedia>> to other resources")
        );
    }

    /**
     * Intervention entity Field Description
     *
     * @return see desc
     */
    public static List<FieldDescriptor> getInterventionFieldDescription(final boolean wLinks) {
        List<FieldDescriptor> fieldDescriptors = Lists.newArrayList(
                fieldWithPath("id").description("Intervention id"),
                fieldWithPath("cropId").description("Intervention crop id"),
                fieldWithPath("userId").description("Intervention creator id"),
                fieldWithPath("interventionType").description("Intervention type"),
                fieldWithPath("status").description("Intervention status"),
                fieldWithPath("interventionDate").description("Intervention date"),
                fieldWithPath("creationDate").description("Intervention creation date"),
                fieldWithPath("attributes").description("Intervention name table")
        );
        if (wLinks) {
            fieldDescriptors.add(fieldWithPath("_links").description("<<overview-hypermedia>> to other resources"));
        }
        return fieldDescriptors;
    }

    /**
     * Machine data Field Description
     *
     * @return see desc
     */
    public static List<FieldDescriptor> getMachineFieldDescription() {
        return Lists.newArrayList(
                fieldWithPath("id").description("Machine id"),
                fieldWithPath("type").description("Machine type"),
                fieldWithPath("name").description("Machine name"),
                fieldWithPath("description").description("Machine description"),
                fieldWithPath("width").description("Machine width"),
                fieldWithPath("length").description("Machine length"),
                fieldWithPath("farmId").description("Machine farm Id"),
                fieldWithPath("_links").description("<<overview-hypermedia>> to other resources")
        );
    }


    /**
     * Caption data Field Description
     *
     * @return see desc
     */
    public static List<FieldDescriptor> getCaptionFieldDescription() {
        return Lists.newArrayList(
                fieldWithPath("id").description("Caption id"),
                fieldWithPath("display").description("Caption display (JSON format depending on displayType)")
//                fieldWithPath("displayType").description("Caption display type : " + Joiner.on(", ").join(LayerCaptionDisplayType.values()))
        );
    }

    /**
     * Layer classification Field Description
     *
     * @return see desc
     */
    public static List<FieldDescriptor> getLayerClassificationFieldDescription() {
        return Lists.newArrayList(
                fieldWithPath("id").description("Layer category id"),
                fieldWithPath("name").description("Layer category name"),
                fieldWithPath("types").description("Layer category types table"),
                fieldWithPath("types[].id").description("Layer type id"),
                fieldWithPath("types[].name").description("Layer type name"),
                fieldWithPath("types[].layerCategoryId").description("Layer type category id")
        );
    }

    public static Snippet toResponseField(final List<FieldDescriptor> descriptors) {
        return responseFields(descriptors.toArray(new FieldDescriptor[descriptors.size()]));
    }
}
