class AppliedMaterialsController < ApplicationController
  # GET /applied_materials
  # GET /applied_materials.xml
  def index
    @applied_materials = AppliedMaterial.all

    respond_to do |format|
      format.html # index.html.erb
      format.xml  { render :xml => @applied_materials }
    end
  end

  # GET /applied_materials/1
  # GET /applied_materials/1.xml
  def show
    @applied_material = AppliedMaterial.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @applied_material }
    end
  end

  # GET /applied_materials/new
  # GET /applied_materials/new.xml
  def new
    @applied_material = AppliedMaterial.new

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @applied_material }
    end
  end

  # GET /applied_materials/1/edit
  def edit
    @applied_material = AppliedMaterial.find(params[:id])
  end

  # POST /applied_materials
  # POST /applied_materials.xml
  def create
    @applied_material = AppliedMaterial.new(params[:applied_material])
    @saved = @applied_material.save
  end

  # PUT /applied_materials/1
  # PUT /applied_materials/1.xml
  def update
    @applied_material = AppliedMaterial.find(params[:id])

    respond_to do |format|
      if @applied_material.update_attributes(params[:applied_material])
        format.html { redirect_to(@applied_material, :notice =>AppliedMaterial.model_name.human + ' ' + t(:update_successful)) }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @applied_material.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /applied_materials/1
  # DELETE /applied_materials/1.xml
  def destroy
    @applied_material = AppliedMaterial.find(params[:id])
    @applied_material.destroy
  end
end
