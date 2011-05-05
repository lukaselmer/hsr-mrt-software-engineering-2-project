class TimeEntryTypesController < ApplicationController
  # GET /time_entry_types
  def index
    @time_entry_types = TimeEntryType.active

    respond_to do |format|
      format.html # index.html.erb
    end
  end

  # GET /time_entry_types/1
  def show
    @time_entry_type = TimeEntryType.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
    end
  end

  # GET /time_entry_types/new
  def new
    @time_entry_type = TimeEntryType.new

    respond_to do |format|
      format.html # new.html.erb
    end
  end

  # GET /time_entry_types/1/edit
  def edit
    @time_entry_type = TimeEntryType.find(params[:id])
  end

  # POST /time_entry_types
  def create
    @time_entry_type = TimeEntryType.new(params[:time_entry_type])

    respond_to do |format|
      if @time_entry_type.save
        format.html { redirect_to(@time_entry_type, :notice => TimeEntryType.model_name.human + ' ' + t(:create_successful)) }
      else
        format.html { render :action => "new" }
      end
    end
  end

  # PUT /time_entry_types/1
  def update
    old_type = TimeEntryType.find(params[:id])
    @time_entry_type = TimeEntryType.new(params[:time_entry_type])

    respond_to do |format|
      if old_type.update_attribute(:valid_until, Time.now) && @time_entry_type.save
        format.html { redirect_to(@time_entry_type, :notice => TimeEntryType.model_name.human + ' ' + t(:update_successful)) }
      else
        format.html { render :action => "edit" }
      end
    end
  end

  # DELETE /time_entry_types/1
  def destroy
    @time_entry_type = TimeEntryType.find(params[:id])
    @time_entry_type.destroy

    respond_to do |format|
      format.html { redirect_to(time_entry_types_url) }
    end
  end

  def add_material
    @time_entry_type = TimeEntryType.find(params[:id])
    @time_entry_type_material = TimeEntryTypeMaterial.new(:time_entry_type => @time_entry_type)
  end

  def synchronize
    @updated_time_entry_types = TimeEntryType.updated_after(params[:last_update])
    render :json => @updated_time_entry_types
  end
end
