# The MaterialsController is responsible for managing all the materials in the system. It supports all CRUD operations for a Material.
class MaterialsController < ApplicationController
  # GET /materials
  def index
    @materials = Material.active

    respond_to do |format|
      format.html # index.html.erb
    end
  end

  # GET /materials/1
  def show
    @material = Material.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
    end
  end

  # GET /materials/new
  def new
    @material = Material.new

    respond_to do |format|
      format.html # new.html.erb
    end
  end

  # GET /materials/1/edit
  def edit
    @material = Material.find(params[:id])
  end

  # POST /materials
  def create
    @material = Material.new(params[:material])

    respond_to do |format|
      if @material.save
        format.html { redirect_to(@material, :notice => Material.model_name.human + ' ' + t(:create_successful)) }
      else
        format.html { render :action => "new" }
      end
    end
  end

  # PUT /materials/1
  def update
    old_material = Material.find(params[:id])
    @material = Material.new(params[:material])

    respond_to do |format|
      if old_material.update_attribute(:valid_until, Time.now) && @material.save
        format.html { redirect_to(@material, :notice => Material.model_name.human + ' ' + t(:update_successful)) }
      else
        format.html { render :action => "edit" }
      end
    end
  end

  # Really delete material
  def delete
    @material = Material.find(params[:id])
    @material.destroy

    respond_to do |format|
      format.html { redirect_to(materials_url) }
    end
  end

  # DELETE /materials/1 to historize material
  def destroy
    @material = Material.find(params[:id])
    @material.update_attribute(:valid_until, Time.now)
      
    redirect_to(materials_url)
  end
end
