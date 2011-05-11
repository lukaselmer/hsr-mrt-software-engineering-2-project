class TimeEntriesController < ApplicationController

  # GET /time_entries
  def index
    @time_entries = TimeEntry.all

    respond_to do |format|
      format.html # index.html.erb
    end
  end

  def unassigned
    @time_entries = TimeEntry.unassigned

    respond_to do |format|
      format.html { render :template => "time_entries/index", :time_entries => @time_entries }
    end
  end

  # GET /time_entries/1
  # GET /time_entries/1.xml
  def show
    @time_entry = TimeEntry.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
    end
  end

  # GET /time_entries/new
  def new
    @time_entry = TimeEntry.new(:order_id => params[:order_id])

    respond_to do |format|
      format.html # new.html.erb
    end
  end

  # GET /time_entries/1/edit
  def edit
    @time_entry = TimeEntry.find(params[:id])
    
    deny_access! and return unless write_access?(@time_entry)
  end

  # POST /time_entries
  def create
    
    respond_to do |format|
      format.html { create_by_html }
      format.json { create_by_json }
    end
  end

  private
  def create_by_html
    @time_entry = TimeEntry.new(params[:time_entry])
    @time_entry.hashcode = nil
    if @time_entry.save
      redirect_to(@time_entry, :notice => TimeEntry.model_name.human + ' ' + t(:create_successful))
    else
      render :action => "new"
    end
  end

  def create_by_json
    Rails.logger.debug params.inspect

    # Case 1: Hashcode is blank and error is rendered
    render :json => "Hashcode blank", :status => :unprocessable_entity and return if params[:time_entry][:hashcode].blank?

    # Case 2: TimeEntry exists and is rendered
    @time_entry = TimeEntry.find_by_hashcode(params[:time_entry][:hashcode])
    render :json => @time_entry, :status => :created, :location => @time_entry and return if !@time_entry.nil?

    # Case 3: TimeEntry does not exists and is created
    @gps_position = GpsPosition.new(params[:time_entry][:gps_position_data])
    params[:time_entry].delete :gps_position_data

    @time_entry = TimeEntry.new(params[:time_entry])
    @time_entry.gps_position = @gps_position;
    @time_entry.user = current_user
    
    render :json => @time_entry.errors, :status => :unprocessable_entity and return if !@time_entry.save
    render :json => @time_entry, :status => :created, :location => @time_entry
  end

  public
  # PUT /time_entries/1
  def update
    @time_entry = TimeEntry.find(params[:id])
    
    deny_access! and return unless write_access?(@time_entry)

    respond_to do |format|
      if @time_entry.update_attributes(params[:time_entry])
        format.html { redirect_to(@time_entry, :notice => TimeEntry.model_name.human + ' ' + t(:update_successful)) }
      else
        format.html { render :action => "edit" }
      end
    end
  end

  # DELETE /time_entries/1
  def destroy
    @time_entry = TimeEntry.find(params[:id])
    deny_access! and return unless write_access?(@time_entry)
    @time_entry.destroy

    respond_to do |format|
      format.html { redirect_to(time_entries_url) }
    end
  end

  # POST /time_entries/1/remove_hashcode
  def remove_hashcode
    @time_entry = TimeEntry.find(params[:id])
    @time_entry.remove_hashcode
    
    respond_to do |format|
      format.html { redirect_to(@time_entry, :notice => TimeEntry.model_name.human + ' ' + t(:persist_successful)) }
      format.json { render :json => @time_entry, :status => :ok, :location => @time_entry  }
    end
  end
end
