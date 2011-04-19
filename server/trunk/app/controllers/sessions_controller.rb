class SessionsController < Devise::SessionsController
  def self.create
    resource = warden.authenticate!(:scope => resource_name, :recall => "#{controller_path}#new")
    set_flash_message(:notice, :signed_in) if is_navigational_format?
    sign_in(resource_name, resource)
    respond_with(resource) do |format|
      format.html { render :location => redirect_location(resource_name, resource) }
      format.json { render :json => current_user, :status => :ok }
    end
  end
end
