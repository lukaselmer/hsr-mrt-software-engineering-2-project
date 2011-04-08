class TimeEntriesController < ApplicationController
  # GET /time_entries
  def index
    @time_entries = TimeEntry.all

    respond_to do |format|
      format.html # index.html.erb
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
    @time_entry = TimeEntry.new

    respond_to do |format|
      format.html # new.html.erb
    end
  end

  # GET /time_entries/1/edit
  def edit
    @time_entry = TimeEntry.find(params[:id])
  end

  # POST /time_entries
  def create
    @time_entry = TimeEntry.find_by_hashcode(params[:time_entry][:hashcode]) || TimeEntry.new(params[:time_entry])

    respond_to do |format|
      if params[:hashcode] != nil && (!@time_entry.new_record?) || @time_entry.save
        format.html { redirect_to(@time_entry, :notice => 'Time entry was successfully created.') }
        format.json { render :json => @time_entry, :status => :created, :location => @time_entry  }
      else
        format.html { render :action => "new" }
        format.json  { render :json => @time_entry.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /time_entries/1
  def update
    @time_entry = TimeEntry.find(params[:id])

    respond_to do |format|
      if @time_entry.update_attributes(params[:time_entry])
        format.html { redirect_to(@time_entry, :notice => 'Time entry was successfully updated.') }
      else
        format.html { render :action => "edit" }
      end
    end
  end

  # DELETE /time_entries/1
  def destroy
    @time_entry = TimeEntry.find(params[:id])
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
      format.html { redirect_to(@time_entry, :notice => 'Time entry was successfully persisted.') }
      format.json { render :json => @time_entry, :status => :ok, :location => @time_entry  }
    end
  end
end
