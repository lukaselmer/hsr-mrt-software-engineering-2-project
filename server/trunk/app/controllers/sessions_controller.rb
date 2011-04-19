class SessionsController < Devise::SessionsController
  
  def create

    respond_to do |format|
      format.html { super }
      format.json {
        resource = warden.authenticate(:scope => resource_name, :recall => "#{controller_path}#new")
        render :status => 200, :json => resource.to_json(:except => [ :encrypted_password, :password_salt ]) and return unless resource.nil?
        render :status => 406, :json => { :error => :invalid }
      }
    end
  end
end
