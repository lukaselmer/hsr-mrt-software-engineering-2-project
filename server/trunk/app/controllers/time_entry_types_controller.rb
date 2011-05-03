class TimeEntryTypesController < ApplicationController
  # GET /time_entry_types
  # GET /time_entry_types.xml
  def index
    @time_entry_types = TimeEntryType.all

    respond_to do |format|
      format.html # index.html.erb
      format.xml  { render :xml => @time_entry_types }
    end
  end

  # GET /time_entry_types/1
  # GET /time_entry_types/1.xml
  def show
    @time_entry_type = TimeEntryType.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @time_entry_type }
    end
  end

  # GET /time_entry_types/new
  # GET /time_entry_types/new.xml
  def new
    @time_entry_type = TimeEntryType.new
    @time_entry_type_materials = TimeEntryTypeMaterial.all

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @time_entry_type }
    end
  end

  # GET /time_entry_types/1/edit
  def edit
    @time_entry_type = TimeEntryType.find(params[:id])
    @time_entry_type_materials = TimeEntryTypeMaterial.all
  end

  # POST /time_entry_types
  # POST /time_entry_types.xml
  def create
    @time_entry_type = TimeEntryType.new(params[:time_entry_type])

    respond_to do |format|
      if @time_entry_type.save
        format.html { redirect_to(@time_entry_type, :notice => TimeEntryType.model_name.human + ' ' + t(:create_successful)) }
        format.xml  { render :xml => @time_entry_type, :status => :created, :location => @time_entry_type }
      else
        format.html { render :action => "new" }
        format.xml  { render :xml => @time_entry_type.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /time_entry_types/1
  # PUT /time_entry_types/1.xml
  def update
    @time_entry_type = TimeEntryType.find(params[:id])

    respond_to do |format|
      if @time_entry_type.update_attributes(params[:time_entry_type])
        format.html { redirect_to(@time_entry_type, :notice => TimeEntryType.model_name.human + ' ' + t(:update_successful)) }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @time_entry_type.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /time_entry_types/1
  # DELETE /time_entry_types/1.xml
  def destroy
    @time_entry_type = TimeEntryType.find(params[:id])
    @time_entry_type.destroy

    respond_to do |format|
      format.html { redirect_to(time_entry_types_url) }
      format.xml  { head :ok }
    end
  end
end
