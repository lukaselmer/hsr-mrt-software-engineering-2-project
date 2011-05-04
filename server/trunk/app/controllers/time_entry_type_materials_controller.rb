class TimeEntryTypeMaterialsController < ApplicationController
  # GET /time_entry_type_materials
  # GET /time_entry_type_materials.xml
  def index
    @time_entry_type_materials = TimeEntryTypeMaterial.all

    respond_to do |format|
      format.html # index.html.erb
      format.xml  { render :xml => @time_entry_type_materials }
    end
  end

  # GET /time_entry_type_materials/1
  # GET /time_entry_type_materials/1.xml
  def show
    @time_entry_type_material = TimeEntryTypeMaterial.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @time_entry_type_material }
    end
  end

  # GET /time_entry_type_materials/new
  # GET /time_entry_type_materials/new.xml
  def new
    @time_entry_type_material = TimeEntryTypeMaterial.new

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @time_entry_type_material }
    end
  end

  # GET /time_entry_type_materials/1/edit
  def edit
    @time_entry_type_material = TimeEntryTypeMaterial.find(params[:id])
  end

  # POST /time_entry_type_materials
  # POST /time_entry_type_materials.xml
  def create
    @time_entry_type_material = TimeEntryTypeMaterial.new(params[:time_entry_type_material])
    @saved = @time_entry_type_material.save
  end

  # PUT /time_entry_type_materials/1
  # PUT /time_entry_type_materials/1.xml
  def update
    @time_entry_type_material = TimeEntryTypeMaterial.find(params[:id])

    respond_to do |format|
      if @time_entry_type_material.update_attributes(params[:time_entry_type_material])
        format.html { redirect_to(@time_entry_type_material, :notice => 'Time entry type material was successfully updated.') }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @time_entry_type_material.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /time_entry_type_materials/1
  # DELETE /time_entry_type_materials/1.xml
  def destroy
    @time_entry_type_material = TimeEntryTypeMaterial.find(params[:id])
    @time_entry_type_material.destroy
  end
end
