class SessionsController < Devise::SessionsController
  def create
    respond_to do |format|
      format.html { super }
      format.json {
        warden.authenticate!( :scope => resource_name ); render :status => 200, :json => current_user.to_json(:except => [ :encrypted_password, :password_salt ]) and return unless current_user.nil?
      }
    end
  end
end
